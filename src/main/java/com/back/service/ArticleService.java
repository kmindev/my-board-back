package com.back.service;

import com.back.domain.Article;
import com.back.domain.Hashtag;
import com.back.domain.UserAccount;
import com.back.domain.constant.SearchType;
import com.back.exception.ArticleNotFoundException;
import com.back.exception.UnexpectedSearchTypeException;
import com.back.exception.UserMismatchException;
import com.back.repository.ArticleRepository;
import com.back.service.dto.ArticleUpdateDto;
import com.back.service.dto.ArticleWithCommentsWithHashtagsDto;
import com.back.service.dto.ArticleWithHashtagsDto;
import com.back.service.dto.NewArticleRequestDto;
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

    private Article findArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
    }

    public ArticleWithHashtagsDto newArticle(NewArticleRequestDto dto) {
        UserAccount userAccount = userAccountService.getUserAccount(dto.userId());
        Article article = dto.newArticle(userAccount);
        Article savedArticle = articleRepository.save(article);

        Set<Hashtag> hashtags = hashtagService.extractAndSaveHashtags(dto.content());
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
        return ArticleWithCommentsWithHashtagsDto.from(findArticle(articleId));
    }

    public ArticleWithHashtagsDto updateArticle(ArticleUpdateDto dto) {
        Article findArticle = findArticle(dto.id());
        UserAccount userAccount = userAccountService.getUserAccount(dto.userId()); // 작성자

        if (!findArticle.getUserAccount().equals(userAccount)) { // 작성자 일치 여부 검증
            throw new UserMismatchException();
        }

        findArticle.updateTitle(dto.title());
        if (findArticle.getContent().equals(dto.content())) { // 본문이 같으면 해시태그를 추출 x
            return ArticleWithHashtagsDto.from(findArticle);
        }

        Set<Hashtag> extractHashtags = hashtagService.extractAndSaveHashtags(dto.content()); // 해시태그 추출
        findArticle.updateArticleHashtags(extractHashtags); // 해시태그 업데이트
        findArticle.updateContent(dto.content());

        return ArticleWithHashtagsDto.from(findArticle);
    }

    public void deleteArticle(Long articleId, String userId) {
        Article findArticle = findArticle(articleId);
        UserAccount userAccount = userAccountService.getUserAccount(userId);

        if (!findArticle.getUserAccount().equals(userAccount)) {
            throw new UserMismatchException();
        }

        articleRepository.deleteById(articleId);
    }

}
