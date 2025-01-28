package com.back.controler.dto.request;

public class ArticleUpdateRequestFactory {

    private static final String DEFAULT_TITLE = "제목1";
    private static final String DEFAULT_CONTENT = "내용입니다.";

    /**
     * <p>
     * 기본값으로 구성된 {@link ArticleUpdateRequest} 객체를 생성합니다.
     * <ul>
     *   <li>title: {@link ArticleUpdateRequestFactory#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleUpdateRequestFactory#DEFAULT_CONTENT}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link ArticleUpdateRequest} 객체
     */
    public static ArticleUpdateRequest createArticleUpdateRequest() {
        return new ArticleUpdateRequest(DEFAULT_TITLE, DEFAULT_CONTENT);
    }

}
