package com.back.service.dto;

import com.back.domain.Article;
import com.back.domain.UserAccount;

public record NewArticleRequestDto(
        String title, // 제목
        String content, // 내용
        String userId // 사용자 id
) {
    public Article newArticle(UserAccount userAccount) {
        return Article.newArticle(userAccount, this.title, this.content);
    }
}
