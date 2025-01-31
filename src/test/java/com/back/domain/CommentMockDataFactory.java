package com.back.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class CommentMockDataFactory {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_CONTENT = "댓글입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link Comment} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link CommentMockDataFactory#DEFAULT_ID}</li>
     *   <li>content: {@link CommentMockDataFactory#DEFAULT_CONTENT}</li>
     *   <li>article: {@param article}</li>
     *   <li>userAccount: {@param userAccount}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link Comment} 객체
     */
    public static Comment createDBComment(Article article, UserAccount userAccount) {
        Comment comment = Comment.newComment(article, userAccount, DEFAULT_CONTENT);
        ReflectionTestUtils.setField(comment, "id", DEFAULT_ID);
        return comment;
    }

}
