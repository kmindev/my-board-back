package com.back.service.dto;

import com.back.domain.Article;
import com.back.domain.ArticleHashtag;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;


public record ArticleWithHashtagsDto(
        Long id, // id
        String title, // 제목
        String content, // 내용
        UserAccountDto userAccountDto, // 사용자 id
        Set<HashtagDto> hashtagDtos,
        LocalDateTime createdAt, // 작성일시
        String createdBy, // 작성자
        LocalDateTime modifiedAt, // 수정일시
        String modifiedBy // 수정자
) {

    public static ArticleWithHashtagsDto from(Article article) {
        return new ArticleWithHashtagsDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                UserAccountDto.from(article.getUserAccount()),
                article.getArticleHashtags()
                        .stream()
                        .map(ArticleHashtag::getHashtag)
                        .map(HashtagDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                article.getCreatedAt(),
                article.getCreatedBy(),
                article.getModifiedAt(),
                article.getModifiedBy()
        );
    }

}
