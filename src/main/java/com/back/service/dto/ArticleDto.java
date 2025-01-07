package com.back.service.dto;

import com.back.controler.dto.reponse.ArticleResponse;
import com.back.domain.Article;
import com.back.domain.UserAccount;

import java.time.LocalDateTime;


public record ArticleDto(
        Long id, // id
        String title, // 제목
        String content, // 내용
        String userId, // 사용자 id
        LocalDateTime createdAt, // 작성일시
        String createdBy, // 작성자
        LocalDateTime modifiedAt, // 수정일시
        String modifiedBy // 수정자
) {

    public static ArticleDto of(
            Long id, String title, String content, String userId,
            LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy
    ) {
        return new ArticleDto(id, title, content, userId, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleDto of(
            String title, String content, String userId,
            LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy
    ) {
        return of(null, title, content, userId, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleDto of(String title, String content, String userId) {
        return of(title, content, userId, null, null, null, null);
    }

    public static ArticleDto of(Article article) {
        return of(
                article.getId(), article.getTitle(), article.getContent(), article.getUserAccount().getUserId(),
                article.getCreatedAt(), article.getCreatedBy(), article.getModifiedAt(), article.getModifiedBy()
        );
    }

    public Article newArticle(UserAccount userAccount) {
        return Article.newArticle(userAccount, this.title, this.content);
    }

    public ArticleResponse toResponse() {
        return new ArticleResponse(this.id, this.title, this.content, this.createdAt, this.createdBy);
    }

}
