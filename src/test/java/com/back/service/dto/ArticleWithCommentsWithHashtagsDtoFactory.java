package com.back.service.dto;

import java.util.Set;

import static com.back.service.dto.CommentDtoFactory.createCommentDto;
import static com.back.service.dto.HashtagDtoFactory.createHashtagDto;
import static com.back.service.dto.UserAccountDtoFactory.createUserAccountDto;

public class ArticleWithCommentsWithHashtagsDtoFactory {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_TITLE = "제목";
    private static final String DEFAULT_CONTENT = "내용입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link ArticleWithCommentsWithHashtagsDto} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link ArticleWithCommentsWithHashtagsDtoFactory#DEFAULT_ID}</li>
     *   <li>commentDtos: {@link CommentDtoFactory#createCommentDto()}</li>
     *   <li>hashtagDtos: {@link HashtagDtoFactory#createHashtagDto()}</li>
     *   <li>userAccountDto: {@link UserAccountDtoFactory#createUserAccountDto()}</li>
     *   <li>title: {@link ArticleWithCommentsWithHashtagsDtoFactory#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleWithCommentsWithHashtagsDtoFactory#DEFAULT_CONTENT}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link ArticleWithCommentsWithHashtagsDto} 객체
     */
    public static ArticleWithCommentsWithHashtagsDto createArticleWithCommentsWithHashtagsDto() {
        return new ArticleWithCommentsWithHashtagsDto(
                DEFAULT_ID,
                Set.of(createCommentDto()),
                Set.of(createHashtagDto()),
                createUserAccountDto(),
                DEFAULT_TITLE,
                DEFAULT_CONTENT,
                null,
                null,
                null,
                null
        );
    }

}
