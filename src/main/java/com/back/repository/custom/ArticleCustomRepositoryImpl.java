package com.back.repository.custom;

import com.back.domain.Article;
import com.back.domain.QArticle;
import com.back.domain.QArticleHashtag;
import com.back.domain.QHashtag;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class ArticleCustomRepositoryImpl extends QuerydslRepositorySupport implements ArticleCustomRepository{

    public ArticleCustomRepositoryImpl() {
        super(Article.class);
    }

    @Override
    public Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
        QArticle article = QArticle.article;
        QHashtag hashtag = QHashtag.hashtag;
        QArticleHashtag articleHashtag = QArticleHashtag.articleHashtag;

        JPQLQuery<Article> query = from(article)
                .innerJoin(article.articleHashtags, articleHashtag)
                .innerJoin(articleHashtag.hashtag, hashtag)
                .where(hashtag.hashtagName.in(hashtagNames));
        List<Article> articles = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(articles, pageable, articles.size());
    }

}
