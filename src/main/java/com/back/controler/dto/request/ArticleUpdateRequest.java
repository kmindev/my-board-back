package com.back.controler.dto.request;

import com.back.service.dto.ArticleUpdateDto;
import com.back.service.dto.UserAccountDto;
import jakarta.validation.constraints.NotBlank;

public record ArticleUpdateRequest(
        @NotBlank(message = "제목을 입력하세요.") String title,
        @NotBlank(message = "내용을 입력하세요.") String content
) {

    public ArticleUpdateDto toDto(Long articleId, UserAccountDto userAccountDto) {
        return new ArticleUpdateDto(articleId, this.title, this.content, userAccountDto.userId());
    }

}
