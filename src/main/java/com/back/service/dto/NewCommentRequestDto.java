package com.back.service.dto;

public record NewCommentRequestDto(
        Long articleId,
        String content,
        String userId
) {
}
