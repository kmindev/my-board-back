package com.back.service;

import com.back.domain.Article;
import com.back.domain.ArticleHashtag;
import com.back.domain.Hashtag;
import com.back.domain.UserAccount;
import com.back.repository.ArticleHashtagRepository;
import com.back.repository.ArticleRepository;
import com.back.service.dto.ArticleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.back.domain.ArticleMockDataFactoryTest.createDBArticleFromUserAccount;
import static com.back.domain.HashtagMockDataFactory.createDBHashtagFromIdAndHashtagName;
import static com.back.domain.UserAccountMockDataFactory.createDBUserAccountFromUserId;
import static com.back.service.dto.ArticleDtoFactory.createArticleDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ArticleHashtagRepository articleHashtagRepository;
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
        UserAccount userAccount = createDBUserAccountFromUserId(articleDto.userId());
        Set<Hashtag> hashtags = Set.of(
                createDBHashtagFromIdAndHashtagName(1L, "HASHTAG1"),
                createDBHashtagFromIdAndHashtagName(2L, "HASHTAG2")
        );
        Article savedArticle = createDBArticleFromUserAccount(userAccount);

        given(userAccountService.getUserAccount(articleDto.userId())).willReturn(userAccount);
        given(articleRepository.save(any(Article.class))).willReturn(savedArticle);
        given(hashtagService.extractAndSaveHashtags(articleDto.content())).willReturn(hashtags);
        given(articleHashtagRepository.save(any(ArticleHashtag.class)))
                .willAnswer(invocation -> invocation.getArgument(0)); // 파라미터가 그대로 반환

        // When
        ArticleDto result = sut.newArticle(articleDto);

        // Then
        assertThat(result.id()).isEqualTo(savedArticle.getId());
        then(userAccountService).should().getUserAccount(articleDto.userId());
        then(articleRepository).should().save(any(Article.class));
        then(hashtagService).should().extractAndSaveHashtags(articleDto.content());
        then(articleHashtagRepository).should(times(hashtags.size())).save(any(ArticleHashtag.class));
    }

}