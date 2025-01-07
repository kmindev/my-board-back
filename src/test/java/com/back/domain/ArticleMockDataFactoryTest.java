package com.back.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class ArticleMockDataFactoryTest {

    private static final String DEFAULT_TITLE = "제목1";
    private static final String DEFAULT_CONTENT = "내용입니다.";


    /**
     * <p>
     * 기본값으로 구성된 {@link Article} 객체를 생성합니다.
     * <ul>
     *   <li>id: {@code 1L}</li>
     *   <li>title: {@link ArticleMockDataFactoryTest#DEFAULT_TITLE}</li>
     *   <li>content: {@link ArticleMockDataFactoryTest#DEFAULT_CONTENT}</li>
     *   <li>UserAccount: {@param userAccount}</li>
     * </ul>
     * </p>
     *
     * @return 기본값으로 구성된 {@link Article} 객체
     */
    public static Article createDBArticleFromUserAccount(UserAccount userAccount) {
        Article article = Article.newArticle(userAccount, DEFAULT_TITLE, DEFAULT_CONTENT);
        ReflectionTestUtils.setField(article, "id", 1L);
        return article;
    }

}