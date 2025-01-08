package com.back.service.dto;

public class ArticleDtoFactory {

    private static final String DEFAULT_TITLE = "제목";
    private static final String DEFAULT_CONTENT = "내용입니다.";
    private static final String DEFAULT_USER_ID = "user1";

    /**
     * <p>
     * 기본값으로 구성된 {@link ArticleDto} 객체를 생성합니다.
     * <ul>
     *   <li>title: {@link ArticleDtoFactory#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleDtoFactory#DEFAULT_CONTENT}</li>
     *   <li>userId: {@link ArticleDtoFactory#DEFAULT_USER_ID}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link ArticleDto} 객체
     */
    public static ArticleDto createArticleDto() {
        return ArticleDto.of(DEFAULT_TITLE, DEFAULT_CONTENT, DEFAULT_USER_ID);
    }

}