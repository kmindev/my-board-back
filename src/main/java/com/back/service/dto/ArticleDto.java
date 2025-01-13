package com.back.service.dto;

import com.back.domain.Article;
import com.back.domain.UserAccount;

import java.time.LocalDateTime;


public record ArticleDto(
        Long id, // id
        String title, // 제목
        String content, // 내용
        UserAccountDto userAccountDto, // 사용자 id
        LocalDateTime createdAt, // 작성일시
        String createdBy, // 작성자
        LocalDateTime modifiedAt, // 수정일시
        String modifiedBy // 수정자
) {

    public static ArticleDto of(
            Long id, String title, String content, UserAccountDto userAccountDto,
            LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy
    ) {
        return new ArticleDto(id, title, content, userAccountDto, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleDto of(
            String title, String content, UserAccountDto userAccountDto,
            LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy
    ) {
        return of(null, title, content, userAccountDto, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleDto of(String title, String content, UserAccountDto userAccountDto) {
        return of(title, content, userAccountDto, null, null, null, null);
    }

    public Article newArticle(UserAccount userAccount) {
        return Article.newArticle(userAccount, this.title, this.content);
    }

}
