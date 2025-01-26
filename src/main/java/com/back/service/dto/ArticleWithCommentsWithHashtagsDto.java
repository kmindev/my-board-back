package com.back.service.dto;

import com.back.domain.Article;
import com.back.domain.ArticleHashtag;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsWithHashtagsDto(
        Long id, // id
        Set<CommentDto> commentDtos, // 댓글
        Set<HashtagDto> hashtagDtos, // 해시태그
        UserAccountDto userAccountDto, // 작성자
        String title, // 제목
        String content, // 내용
        LocalDateTime createdAt, // 작성일시
        String createdBy, // 작성자
        LocalDateTime modifiedAt, // 수정일시
        String modifiedBy // 수정자
) {

    public static ArticleWithCommentsWithHashtagsDto from(Article entity) {
        return new ArticleWithCommentsWithHashtagsDto(
                entity.getId(),
                entity.getComments().stream()
                        .map(CommentDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getArticleHashtags().stream()
                        .map(ArticleHashtag::getHashtag)
                        .map(HashtagDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

}
