package com.back.service.dto;

import com.back.domain.Hashtag;

import java.time.LocalDateTime;

public record HashtagDto(
        Long id,
        String hashtagName,
        LocalDateTime createdAt, // 작성일시
        String createdBy, // 작성자
        LocalDateTime modifiedAt, // 수정일시
        String modifiedBy // 수정자
) {

    public static HashtagDto from(Hashtag hashtag) {
        return new HashtagDto(
                hashtag.getId(),
                hashtag.getHashtagName(),
                hashtag.getCreatedAt(),
                hashtag.getCreatedBy(),
                hashtag.getModifiedAt(),
                hashtag.getModifiedBy()
        );
    }

}
