package com.back.domain;

import org.springframework.test.util.ReflectionTestUtils;

import static com.back.domain.UserAccountMockDataFactory.createDBUserAccount;


public class ArticleMockDataFactory {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_TITLE = "제목1";
    private static final String DEFAULT_CONTENT = "내용입니다.";


    /**
     * <p>
     * 기본값으로 구성된 {@link Article} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@link ArticleMockDataFactory#DEFAULT_ID}</li>
     *   <li>title: {@link ArticleMockDataFactory#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleMockDataFactory#DEFAULT_CONTENT}</li>
     *   <li>UserAccount: {@param userAccount}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link Article} 객체
     */
    public static Article createDBArticleFromUserAccount(UserAccount userAccount) {
        Article article = Article.newArticle(userAccount, DEFAULT_TITLE, DEFAULT_CONTENT);
        ReflectionTestUtils.setField(article, "id", DEFAULT_ID);
        return article;
    }

    /**
     * <p>
     * 기본값으로 구성된 {@link Article} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@param articleId}</li>
     *   <li>title: {@link ArticleMockDataFactory#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleMockDataFactory#DEFAULT_CONTENT}</li>
     *   <li>UserAccount: {@link UserAccountMockDataFactory#createDBUserAccount()}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link Article} 객체
     */
    public static Article createDBArticleFromArticleId(Long articleId) {
        Article article = Article.newArticle(createDBUserAccount(), DEFAULT_TITLE, DEFAULT_CONTENT);
        ReflectionTestUtils.setField(article, "id", articleId);
        return article;
    }

}