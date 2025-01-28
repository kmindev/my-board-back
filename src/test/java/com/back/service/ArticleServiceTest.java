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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

import static com.back.domain.ArticleMockDataFactory.*;
import static com.back.domain.HashtagMockDataFactory.createDBHashtagFromIdAndHashtagName;
import static com.back.domain.UserAccountMockDataFactory.createDBUserAccountFromUserId;
import static com.back.service.dto.ArticleUpdateDtoFactory.createArticleUpdateDto;
import static com.back.service.dto.NewArticleRequestDtoFactory.createNewArticleRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

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
        NewArticleRequestDto newArticleRequestDto = createNewArticleRequestDto();
        UserAccount userAccount = createDBUserAccountFromUserId(newArticleRequestDto.userId());
        Set<Hashtag> hashtags = Set.of(
                createDBHashtagFromIdAndHashtagName(1L, "HASHTAG1"),
                createDBHashtagFromIdAndHashtagName(2L, "HASHTAG2")
        );
        Article savedArticle = createDBArticleFromUserAccount(userAccount);

        given(userAccountService.getUserAccount(newArticleRequestDto.userId())).willReturn(userAccount);
        given(articleRepository.save(any(Article.class))).willReturn(savedArticle);
        given(hashtagService.extractAndSaveHashtags(newArticleRequestDto.content())).willReturn(hashtags);

        // When
        ArticleWithHashtagsDto result = sut.newArticle(newArticleRequestDto);

        // Then
        assertThat(result.id()).isEqualTo(savedArticle.getId());
        then(userAccountService).should().getUserAccount(newArticleRequestDto.userId());
        then(articleRepository).should().save(any(Article.class));
        then(hashtagService).should().extractAndSaveHashtags(newArticleRequestDto.content());
    }

    @DisplayName("검색어와 검색타입 없이 게시글을 검색하면, 페이징 된 게시글 정보를 반환한다.")
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

    @DisplayName("게시글 id를 입력하면, 게시글 id에 해당하는 게시글 상세 정보를 반환한다.")
    @Test
    void givenArticleId_whenGetArticleDetails_thenReturnsArticleWithCommentsWithHashtags() {
        // Given
        Long articleId = 1L;
        Article article = createDBArticleFromArticleId(articleId);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleWithCommentsWithHashtagsDto result = sut.getArticleDetails(articleId);

        // Then
        assertThat(result.id()).isEqualTo(articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("존재하지 않는 게시글 id를 입력하면, 예외가 발생한다.")
    @Test
    void givenNonExitingArticleId_whenGetArticleDetails_thenThrowsException() {
        // Given
        Long nonExitingArticleId = 100L;
        given(articleRepository.findById(nonExitingArticleId)).willReturn(Optional.empty());

        // When
        ArticleNotFoundException result = assertThrows(ArticleNotFoundException.class,
                () -> sut.getArticleDetails(nonExitingArticleId)
        );

        // Then
        assertThat(result).isInstanceOf(ArticleNotFoundException.class);
        then(articleRepository).should().findById(nonExitingArticleId);
    }

    @DisplayName("게시글 업데이트 정보를 전달하면, 게시글과 게시글과 연관된 해시태그를 업데이트 한다.")
    @Test
    void givenArticleUpdateInfo_whenUpdateArticle_thenUpdatesArticleAndReturnsArticleWithHashtags() {
        // Given
        Long articleId = 1L;
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";
        String userId = "user1";
        Set<Hashtag> hashtags = Set.of(
                createDBHashtagFromIdAndHashtagName(1L, "HASHTAG1"),
                createDBHashtagFromIdAndHashtagName(2L, "HASHTAG2")
        );
        ArticleUpdateDto articleUpdateDto = createArticleUpdateDto(
                articleId,
                updatedTitle,
                updatedContent,
                userId
        );
        UserAccount userAccount = createDBUserAccountFromUserId(userId);
        Article existingArticle = createDBArticleFromArticleIdAndUserAccount(articleId, userAccount);

        given(articleRepository.findById(articleId)).willReturn(Optional.of(existingArticle));
        given(userAccountService.getUserAccount(userId)).willReturn(userAccount);
        given(hashtagService.extractAndSaveHashtags(updatedContent)).willReturn(hashtags);

        // When
        ArticleWithHashtagsDto result = sut.updateArticle(articleUpdateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(articleId);
        assertThat(result.title()).isEqualTo(updatedTitle);
        assertThat(result.content()).isEqualTo(updatedContent);
        assertThat(result.hashtagDtos()).hasSize(hashtags.size());

        then(articleRepository).should().findById(articleId);
        then(userAccountService).should().getUserAccount(userId);
        then(hashtagService).should().extractAndSaveHashtags(updatedContent);
    }

    @DisplayName("게시글 업데이트 시 작성자가 일치하지 않으면, 예외가 발생한다.")
    @Test
    void givenArticleUpdateInfo_whenUpdateArticle_thenThrowsException() {
        // Given
        Long articleId = 1L;
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";
        String userId = "user1";
        ArticleUpdateDto articleUpdateDto = createArticleUpdateDto(
                articleId,
                updatedTitle,
                updatedContent,
                userId
        );
        String otherUserAccountId = "user2";
        UserAccount userAccount = createDBUserAccountFromUserId(userId);
        UserAccount otherUserAccount = createDBUserAccountFromUserId(otherUserAccountId);
        Article existingArticle = createDBArticleFromArticleIdAndUserAccount(articleId, userAccount);

        given(articleRepository.findById(articleId)).willReturn(Optional.of(existingArticle));
        given(userAccountService.getUserAccount(userId)).willReturn(otherUserAccount);

        // When
        UserMismatchException result = assertThrows(UserMismatchException.class,
                () -> sut.updateArticle(articleUpdateDto)
        );

        // Then
        assertThat(result).isInstanceOf(UserMismatchException.class);
        then(articleRepository).should().findById(articleId);
        then(userAccountService).should().getUserAccount(userId);
    }

    @DisplayName("삭제할 게시글 id와 작성자 id를 전달하면, 게시글을 삭제한다.")
    @Test
    void givenArticleIdAndUserId_whenDeleteArticle_thenDeletesArticle() {
        // Given
        Long articleId = 1L;
        String userId = "user1";
        UserAccount userAccount = createDBUserAccountFromUserId(userId);
        Article existingArticle = createDBArticleFromArticleIdAndUserAccount(articleId, userAccount);

        given(articleRepository.findById(articleId)).willReturn(Optional.of(existingArticle));
        given(userAccountService.getUserAccount(userId)).willReturn(userAccount);
        willDoNothing().given(articleRepository).deleteById(articleId);

        // When
        sut.deleteArticle(articleId, userId);

        // Then
        then(articleRepository).should().findById(articleId);
        then(userAccountService).should().getUserAccount(userId);
        then(articleRepository).should().deleteById(articleId);
    }

    @DisplayName("삭제할 게시글과 작성자 일치하지 않으면, 예외가 발생한다.")
    @Test
    void givenArticleIdAndUserId_whenDeleteArticle_thenThrowsException() {
        // Given
        Long articleId = 1L;
        String userId = "user1";
        UserAccount userAccount = createDBUserAccountFromUserId(userId);
        String otherUserAccountId = "user2";
        UserAccount otherUserAccount = createDBUserAccountFromUserId(otherUserAccountId);
        Article existingArticle = createDBArticleFromArticleIdAndUserAccount(articleId, userAccount);

        given(articleRepository.findById(articleId)).willReturn(Optional.of(existingArticle));
        given(userAccountService.getUserAccount(userId)).willReturn(otherUserAccount);

        // When
        UserMismatchException result = assertThrows(UserMismatchException.class,
                () -> sut.deleteArticle(articleId, userId)
        );

        // Then
        assertThat(result).isInstanceOf(UserMismatchException.class);
        then(articleRepository).should().findById(articleId);
        then(userAccountService).should().getUserAccount(userId);
    }


}