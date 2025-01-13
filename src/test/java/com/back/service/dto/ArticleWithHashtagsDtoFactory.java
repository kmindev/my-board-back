package com.back.service.dto;

import java.util.Set;

import static com.back.service.dto.HashtagDtoFactory.createHashtagDto;
import static com.back.service.dto.UserAccountDtoFactory.createUserAccountDto;

public class ArticleWithHashtagsDtoFactory {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_TITLE = "제목";
    private static final String DEFAULT_CONTENT = "내용입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link ArticleWithHashtagsDto} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link ArticleWithHashtagsDtoFactory#DEFAULT_TITLE}</li>
     *   <li>title: {@link ArticleWithHashtagsDtoFactory#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleWithHashtagsDtoFactory#DEFAULT_CONTENT}</li>
     *   <li>userAccountDto: {@link UserAccountDtoFactory#createUserAccountDto()}</li>
     *   <li>hashtagDtos: {@link HashtagDtoFactory#createHashtagDto()} ()}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link ArticleWithHashtagsDto} 객체
     */
    public static ArticleWithHashtagsDto createArticleWithHashtagsDto() {
        return new ArticleWithHashtagsDto(
                DEFAULT_ID, DEFAULT_TITLE, DEFAULT_CONTENT,
                createUserAccountDto(), Set.of(createHashtagDto()),
                null, null, null, null
        );
    }

}
