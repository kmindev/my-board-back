package com.back.controler.dto.reponse;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        String createdBy
) {
}
