package com.back.service;

import com.back.domain.Article;
import com.back.domain.Hashtag;
import com.back.domain.UserAccount;
import com.back.domain.constant.SearchType;
import com.back.exception.ArticleNotFoundException;
import com.back.exception.UnexpectedSearchTypeException;
import com.back.repository.ArticleRepository;
import com.back.service.dto.ArticleDto;
import com.back.service.dto.ArticleWithCommentsWithHashtagsDto;
import com.back.service.dto.ArticleWithHashtagsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final HashtagService hashtagService;
    private final UserAccountService userAccountService;

    public ArticleWithHashtagsDto newArticle(ArticleDto articleDto) {
        UserAccount userAccount = userAccountService.getUserAccount(articleDto.userAccountDto().userId());
        Article article = articleDto.newArticle(userAccount);
        Article savedArticle = articleRepository.save(article);

        Set<Hashtag> hashtags = hashtagService.extractAndSaveHashtags(articleDto.content());
        hashtags.forEach(savedArticle::addHashtag); // Casecade 옵션으로 인해 insert 쿼리 발생

        return ArticleWithHashtagsDto.from(savedArticle);
    }

    @Transactional(readOnly = true)
    public Page<ArticleWithHashtagsDto> searchArticles(Pageable pageable, String searchValue, SearchType searchType) {

        if (searchValue == null || searchValue.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleWithHashtagsDto::from);
        }

        if (searchType == null) {
            throw new UnexpectedSearchTypeException();
        }

        return switch (searchType) {
            case TITLE ->
                    articleRepository.findByTitleContaining(searchValue, pageable).map(ArticleWithHashtagsDto::from);
            case CONTENT ->
                    articleRepository.findByContentContaining(searchValue, pageable).map(ArticleWithHashtagsDto::from);
            case USER_ID -> articleRepository.findByUserAccount_UserIdContaining(searchValue, pageable).map(
                    ArticleWithHashtagsDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchValue, pageable).map(
                    ArticleWithHashtagsDto::from);
            case HASHTAG -> articleRepository.findByHashtagNames(
                            Arrays.stream(searchValue.split(" ")).toList(), pageable)
                    .map(ArticleWithHashtagsDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsWithHashtagsDto getArticleDetails(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsWithHashtagsDto::from)
                .orElseThrow(ArticleNotFoundException::new);
    }

}
