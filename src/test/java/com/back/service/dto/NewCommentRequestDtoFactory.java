package com.back.service.dto;

public class NewCommentRequestDtoFactory {

    private static final Long DEFAULT_ARTICLE_ID = 1L;
    private static final String DEFAULT_CONTENT = "댓글입니다.";
    private static final String DEFAULT_USER_ID = "user1";

    /**
     * <p>
     * 기본값으로 구성된 {@link NewCommentRequestDto} 객체를 생성합니다.
     * <ul>
     *   <li>articleId: {@link NewCommentRequestDtoFactory#DEFAULT_ARTICLE_ID}</li>
     *   <li>content: {@link NewCommentRequestDtoFactory#DEFAULT_CONTENT}</li>
     *   <li>userId: {@link NewCommentRequestDtoFactory#DEFAULT_USER_ID}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link NewCommentRequestDto} 객체
     */
    public static NewCommentRequestDto createNewCommentRequestDto() {
        return new NewCommentRequestDto(DEFAULT_ARTICLE_ID, DEFAULT_CONTENT, DEFAULT_USER_ID);
    }

}
