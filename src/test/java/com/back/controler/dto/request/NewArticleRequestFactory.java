package com.back.controler.dto.request;

public class NewArticleRequestFactory {

    private static final String DEFAULT_TITLE = "제목1";
    private static final String DEFAULT_CONTENT = "내용입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link NewArticleRequest} 객체를 생성합니다.
     * <ul>
     *   <li>title: {@link NewArticleRequestFactory#DEFAULT_TITLE}</li>
     *   <li>content: {@link NewArticleRequestFactory#DEFAULT_CONTENT}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link NewArticleRequest} 객체
     */
    public static NewArticleRequest createDefaultNewArticleRequest() {
        return new NewArticleRequest(DEFAULT_TITLE, DEFAULT_CONTENT);
    }

}