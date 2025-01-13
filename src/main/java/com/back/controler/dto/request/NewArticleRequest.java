package com.back.controler.dto.request;

import com.back.service.dto.ArticleDto;
import com.back.service.dto.UserAccountDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public record NewArticleRequest(
        @NotBlank(message = "제목을 입력하세요.") String title,
        @NotBlank(message = "내용을 입력하세요.") String content
) {
    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return ArticleDto.of(this.title, this.content, userAccountDto);
    }

}
