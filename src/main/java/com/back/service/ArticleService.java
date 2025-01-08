package com.back.service;

import com.back.domain.Article;
import com.back.domain.ArticleHashtag;
import com.back.domain.Hashtag;
import com.back.domain.UserAccount;
import com.back.repository.ArticleHashtagRepository;
import com.back.repository.ArticleRepository;
import com.back.service.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleHashtagRepository articleHashtagRepository;
    private final HashtagService hashtagService;
    private final UserAccountService userAccountService;

    public ArticleDto newArticle(ArticleDto articleDto) {
        UserAccount userAccount = userAccountService.getUserAccount(articleDto.userId());
        Article article = articleDto.newArticle(userAccount);
        Article savedArticle = articleRepository.save(article);

        Set<Hashtag> hashtags = hashtagService.extractAndSaveHashtags(articleDto.content());

        hashtags.forEach(hashtag -> {
            articleHashtagRepository.save(ArticleHashtag.of(savedArticle, hashtag));
        });

        return ArticleDto.of(savedArticle);
    }

}
