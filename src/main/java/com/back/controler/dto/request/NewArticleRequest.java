package com.back.controler.dto.request;

import com.back.service.dto.NewArticleRequestDto;
import com.back.service.dto.UserAccountDto;
import jakarta.validation.constraints.NotBlank;

public record NewArticleRequest(
        @NotBlank(message = "제목을 입력하세요.") String title,
        @NotBlank(message = "내용을 입력하세요.") String content
) {
    public NewArticleRequestDto toDto(UserAccountDto userAccountDto) {
        return new NewArticleRequestDto(this.title, this.content, userAccountDto.userId());
    }

}
