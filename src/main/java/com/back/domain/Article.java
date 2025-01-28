package com.back.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount; // 작성자

    @Column(nullable = false)
    private String title; // 제목
    @Column(nullable = false, length = 65535)
    private String content; // 본문

    @ToString.Exclude
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleHashtag> articleHashtags = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private Article(UserAccount userAccount, String title, String content) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    public static Article newArticle(UserAccount userAccount, String title, String content) {
        return new Article(userAccount, title, content);
    }

    public void addHashtag(Hashtag hashtag) {
        ArticleHashtag articleHashtag = ArticleHashtag.of(this, hashtag);
        this.articleHashtags.add(articleHashtag);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateArticleHashtags(Set<Hashtag> newHashtags) {
        Set<Hashtag> existingHashtags = this.articleHashtags.stream()
                .map(ArticleHashtag::getHashtag)
                .collect(Collectors.toSet());

        Set<Hashtag> removeHashtags = new HashSet<>(existingHashtags);
        removeHashtags.removeAll(newHashtags); // 기존에는 있지만 새로운 해시태그에 없는 것

        Set<Hashtag> addHashtags = new HashSet<>(newHashtags);
        addHashtags.removeAll(existingHashtags); // 새로운 해시태그 중 기존에 없는 것

        addHashtags.forEach(this::addHashtag);  // 추가 처리
        this.getArticleHashtags().removeIf(articleHashtag -> // 삭제 처리
                removeHashtags.contains(articleHashtag.getHashtag())
        );
    }

}
