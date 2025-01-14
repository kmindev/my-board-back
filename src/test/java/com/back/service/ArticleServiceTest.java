package com.back.service;

import com.back.domain.Article;
import com.back.domain.Hashtag;
import com.back.domain.UserAccount;
import com.back.domain.constant.SearchType;
import com.back.exception.UnexpectedSearchTypeException;
import com.back.repository.ArticleRepository;
import com.back.service.dto.ArticleDto;
import com.back.service.dto.ArticleWithHashtagsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Set;

import static com.back.domain.ArticleMockDataFactoryTest.createDBArticleFromUserAccount;
import static com.back.domain.HashtagMockDataFactory.createDBHashtagFromIdAndHashtagName;
import static com.back.domain.UserAccountMockDataFactory.createDBUserAccountFromUserId;
import static com.back.service.dto.ArticleDtoFactory.createArticleDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private HashtagService hashtagService;
    @Mock
    private UserAccountService userAccountService;

    @DisplayName(
            "게시글 정보를 입력하면, 본문에서 해시태그 정보를 추출한 뒤 DB에 반영하고, " +
                    "새로운 해시태그를 저장하며 게시글과 연관 정보를 생성한다."
    )
    @Test
    void givenArticleInfo_whenNewArticle_thenExtractsAndSavesHashtagsAndSavesArticleHashtag() {
        // Given
        ArticleDto articleDto = createArticleDto();
        UserAccount userAccount = createDBUserAccountFromUserId(articleDto.userAccountDto().userId());
        Set<Hashtag> hashtags = Set.of(
                createDBHashtagFromIdAndHashtagName(1L, "HASHTAG1"),
                createDBHashtagFromIdAndHashtagName(2L, "HASHTAG2")
        );
        Article savedArticle = createDBArticleFromUserAccount(userAccount);

        given(userAccountService.getUserAccount(articleDto.userAccountDto().userId())).willReturn(userAccount);
        given(articleRepository.save(any(Article.class))).willReturn(savedArticle);
        given(hashtagService.extractAndSaveHashtags(articleDto.content())).willReturn(hashtags);

        // When
        ArticleWithHashtagsDto result = sut.newArticle(articleDto);

        // Then
        assertThat(result.id()).isEqualTo(savedArticle.getId());
        then(userAccountService).should().getUserAccount(articleDto.userAccountDto().userId());
        then(articleRepository).should().save(any(Article.class));
        then(hashtagService).should().extractAndSaveHashtags(articleDto.content());
    }

    @DisplayName("검색어와 검새타입 없이 게시글을 검색하면, 페이징 된 게시글 정보를 반환한다.")
    @Test
    void givenNoSearchValueAndType_whenSearchArticles_thenReturnsArticlePage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<ArticleWithHashtagsDto> articles = sut.searchArticles(pageable, null, null);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);
    }

    @DisplayName("검색어와 검색타입으로 게시글을 검색하면, 페이징 된 게시글 정보를 반환한다.")
    @Test
    void givenSearchValueAndType_whenSearchArticles_thenReturnsArticlePage() {
        // Given
        String searchValue = "test1";
        SearchType searchType = SearchType.TITLE;
        Pageable pageable = PageRequest.of(0, 10);

        given(articleRepository.findByTitleContaining(searchValue, pageable)).willReturn(Page.empty());

        // When
        Page<ArticleWithHashtagsDto> articles = sut.searchArticles(pageable, searchValue, searchType);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(searchValue, pageable);
    }

    @DisplayName("검색어는 포함되어 있지만 검색타입을 식별할 수 없으면, 예외를 반환한다.")
    @Test
    void givenSearchValueButUnknownSearchType_whenSearchArticles_thenThrowsException() {
        // Given
        String searchValue = "test1";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        UnexpectedSearchTypeException result = assertThrows(UnexpectedSearchTypeException.class,
                () -> sut.searchArticles(pageable, searchValue, null)
        );

        // Then
        assertThat(result).isInstanceOf(UnexpectedSearchTypeException.class);
    }

}