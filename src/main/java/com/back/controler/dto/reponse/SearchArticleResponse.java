package com.back.controler.dto.reponse;

import com.back.domain.constant.SearchType;
import com.back.service.dto.ArticleWithHashtagsDto;
import com.back.service.dto.HashtagDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record SearchArticleResponse(
        Long id,
        String title,
        String content,
        String userId,
        Set<String> hashtagNames,
        Set<String> searchTypes,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static SearchArticleResponse from(ArticleWithHashtagsDto articleWithHashtagsDto) {
        return new SearchArticleResponse(
                articleWithHashtagsDto.id(),
                articleWithHashtagsDto.title(),
                articleWithHashtagsDto.content(),
                articleWithHashtagsDto.userAccountDto().userId(),
                articleWithHashtagsDto.hashtagDtos().stream()
                        .map(HashtagDto::hashtagName)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                Arrays.stream(SearchType.values())
                        .map(SearchType::getTypeName)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                articleWithHashtagsDto.createdAt(),
                articleWithHashtagsDto.createdBy(),
                articleWithHashtagsDto.modifiedAt(),
                articleWithHashtagsDto.modifiedBy()
        );
    }

}
