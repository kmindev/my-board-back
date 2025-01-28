package com.back.service.dto;

public class ArticleUpdateDtoFactory {


    public static ArticleUpdateDto createArticleUpdateDto(Long articleId, String updatedTitle, String updatedContent, String userAccountId) {
        return new ArticleUpdateDto(articleId, updatedTitle, updatedContent, userAccountId);
    }

}
