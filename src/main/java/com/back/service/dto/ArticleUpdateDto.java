package com.back.service.dto;

public record ArticleUpdateDto(
        Long id, // id
        String title, // 제목
        String content, // 본문
        String userId // 작성자
) {
}
