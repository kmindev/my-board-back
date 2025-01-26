package com.back.controler.dto.reponse;

import com.back.service.dto.ArticleWithCommentsWithHashtagsDto;
import com.back.service.dto.HashtagDto;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleDetailsResponse(
        Long id, // id
        String title, // 제목
        String content, // 내용
        String userId, // 작성자
        LocalDateTime createdAt, // 작성일
        String createdBy, // 작성자
        LocalDateTime modifiedAt, // 수정일
        String modifiedBy, // 수정자
        Set<String> hashtagNames, // 해시태그
        Set<CommentResponse> comments // 댓글
) {

    public record CommentResponse(
            Long id,
            String userId, // 작성자
            String content, // 댓글 내용
            LocalDateTime createdAt, // 작성일시
            String createdBy, // 작성자
            LocalDateTime modifiedAt, // 수정일시
            String modifiedBy // 수정자
    ) {

    }

    public static ArticleDetailsResponse from(ArticleWithCommentsWithHashtagsDto dto) {
        return new ArticleDetailsResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.userAccountDto().userId(),
                dto.createdAt(),
                dto.createdBy(),
                dto.modifiedAt(),
                dto.modifiedBy(),
                dto.hashtagDtos().stream()
                        .map(HashtagDto::hashtagName)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                dto.commentDtos().stream()
                        .map(commentDto ->
                                new CommentResponse(
                                        commentDto.id(),
                                        commentDto.userAccountDto().userId(),
                                        commentDto.content(),
                                        commentDto.createdAt(),
                                        commentDto.createdBy(),
                                        commentDto.modifiedAt(),
                                        commentDto.modifiedBy()
                                ))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

}
