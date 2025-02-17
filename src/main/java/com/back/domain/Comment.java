package com.back.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @JoinColumn(name = "article_id")
    @ManyToOne(optional = false)
    private Article article; // 게시글

    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 작성자

    @Column(nullable = false, length = 500) private String content; // 본문

    private Comment(Article article, UserAccount userAccount, String content) {
        this.article = article;
        this.userAccount = userAccount;
        this.content = content;
    }

    public static Comment newComment(Article article, UserAccount userAccount, String content) {
        Comment newComment = new Comment(article, userAccount, content);
        article.getComments().add(newComment);
        return newComment;
    }

}
