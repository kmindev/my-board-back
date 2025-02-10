package com.back.controler;

import com.back.config.JsonDataEncoder;
import com.back.config.UnsecuredWebMvcTest;
import com.back.controler.converter.SearchTypeRequestConverter;
import com.back.controler.dto.request.ArticleUpdateRequest;
import com.back.controler.dto.request.NewArticleRequest;
import com.back.domain.constant.SearchType;
import com.back.exception.ArticleNotFoundException;
import com.back.exception.UnexpectedSearchTypeException;
import com.back.exception.UserMismatchException;
import com.back.exception.UserNotFoundException;
import com.back.service.ArticleService;
import com.back.service.dto.ArticleUpdateDto;
import com.back.service.dto.ArticleWithCommentsWithHashtagsDto;
import com.back.service.dto.ArticleWithHashtagsDto;
import com.back.service.dto.NewArticleRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.back.controler.dto.request.ArticleUpdateRequestFactory.createArticleUpdateRequest;
import static com.back.controler.dto.request.NewArticleRequestFactory.createDefaultNewArticleRequest;
import static com.back.service.dto.ArticleWithCommentsWithHashtagsDtoFactory.createArticleWithCommentsWithHashtagsDto;
import static com.back.service.dto.ArticleWithHashtagsDtoFactory.createArticleWithHashtagsDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("컨트롤러 - 게시글")
@Import({JsonDataEncoder.class})
@UnsecuredWebMvcTest(controllers = ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonDataEncoder jsonDataEncoder;

    @MockitoBean
    private ArticleService articleService;

    private final SearchTypeRequestConverter searchTypeRequestConverter = new SearchTypeRequestConverter();

    @DisplayName("게시글 생성 요청 - 성공")
    @Test
    void givenNewArticleRequest_whenNewArticle_thenReturns200() throws Exception {
        // Given
        NewArticleRequest request = createDefaultNewArticleRequest();
        ArticleWithHashtagsDto articleWithHashtagsDto = createArticleWithHashtagsDto();
        given(articleService.newArticle(any(NewArticleRequestDto.class))).willReturn(articleWithHashtagsDto);

        // When & Then
        mvc.perform(post("/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().newArticle(any(NewArticleRequestDto.class));
    }

    @DisplayName("게시글 생성 요청 - 실패")
    @Test
    void givenNewArticleRequest_whenNewArticle_thenReturns4xx() throws Exception {
        // Given
        NewArticleRequest request = createDefaultNewArticleRequest();
        UserNotFoundException exception = new UserNotFoundException();
        given(articleService.newArticle(any(NewArticleRequestDto.class))).willThrow(exception);

        // When & Then
        mvc.perform(post("/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(articleService).should().newArticle(any(NewArticleRequestDto.class));
    }

    @DisplayName("게시글 조건 검색 - 성공")
    @Test
    void givenSearchParams_whenGetArticles_thenReturns200() throws Exception {
        // Given
        String searchValue = "test1";
        String searchTypeStr = "본문";
        SearchType searchType = searchTypeRequestConverter.convert(searchTypeStr);
        given(articleService.searchArticles(any(Pageable.class), eq(searchValue), eq(searchType)))
                .willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/v1/articles")
                        .queryParam("searchValue", searchValue)
                        .queryParam("searchType", searchType.getTypeName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().searchArticles(any(Pageable.class), eq(searchValue), eq(searchType));
    }

    @DisplayName("게시글 페이징, 정렬 검색 - 성공")
    @Test
    void givenPagingAndSortingParams_whenGetArticles_thenReturns200() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        given(articleService.searchArticles(pageable, null, null))
                .willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/v1/articles")
                        .queryParam("page", String.valueOf(pageNumber))
                        .queryParam("size", String.valueOf(pageSize))
                        .queryParam("sort", sortName + "," + direction))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().searchArticles(pageable, null, null);
    }

    @DisplayName("게시글 검색 - 실패")
    @Test
    void givenNonExitingSearchType_whenNewArticle_thenReturns4xx() throws Exception {
        // Given
        String searchValue = "test1";
        String searchTypeStr = "잘못된 타입";
        SearchType searchType = searchTypeRequestConverter.convert(searchTypeStr);
        UnexpectedSearchTypeException exception = new UnexpectedSearchTypeException();
        given(articleService.searchArticles(any(Pageable.class), eq(searchValue), eq(searchType)))
                .willThrow(exception);

        // When & Then
        mvc.perform(get("/v1/articles")
                        .queryParam("searchValue", searchValue)
                        .queryParam("searchType", searchTypeStr))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(articleService).should().searchArticles(any(Pageable.class), eq(searchValue), eq(searchType));
    }

    @DisplayName("게시글 상세 조회 - 성공")
    @Test
    void givenArticleId_whenGetArticleDetails_thenReturns200() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleWithCommentsWithHashtagsDto dto = createArticleWithCommentsWithHashtagsDto();
        given(articleService.getArticleDetails(eq(articleId))).willReturn(dto);

        // When & Then
        mvc.perform(get("/v1/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().getArticleDetails(eq(articleId));
    }

    @DisplayName("게시글 상세 조회 - 실패")
    @Test
    void givenNonExitingArticleId_whenGetArticleDetails_thenReturns200() throws Exception {
        // Given
        Long nonExitingArticleId = 100L;
        ArticleNotFoundException exception = new ArticleNotFoundException();
        given(articleService.getArticleDetails(eq(nonExitingArticleId))).willThrow(exception);

        // When & Then
        mvc.perform(get("/v1/articles/" + nonExitingArticleId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(articleService).should().getArticleDetails(eq(nonExitingArticleId));
    }

    @DisplayName("게시글 수정 - 성공")
    @Test
    void givenArticleUpdateRequest_whenUpdateArticle_thenReturns200() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleUpdateRequest request = createArticleUpdateRequest();
        ArticleWithHashtagsDto articleWithHashtagsDto = createArticleWithHashtagsDto();
        given(articleService.updateArticle(any(ArticleUpdateDto.class))).willReturn(articleWithHashtagsDto);

        // When & Then
        mvc.perform(patch("/v1/articles/" + articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().updateArticle(any(ArticleUpdateDto.class));
    }

    @DisplayName("게시글 수정 - 실패")
    @Test
    void givenArticleUpdateRequest_whenUpdateArticle_thenReturns4xx() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleUpdateRequest request = createArticleUpdateRequest();
        UserMismatchException exception = new UserMismatchException();
        given(articleService.updateArticle(any(ArticleUpdateDto.class))).willThrow(exception);

        // When & Then
        mvc.perform(patch("/v1/articles/" + articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(articleService).should().updateArticle(any(ArticleUpdateDto.class));
    }


    @DisplayName("게시글 삭제 - 성공")
    @Test
    void givenArticleIdAndUserId_whenDeleteArticle_thenReturns200() throws Exception {
        // Given
        Long articleId = 1L;
        willDoNothing().given(articleService).deleteArticle(any(), any());

        // When & Then
        mvc.perform(delete("/v1/articles/" + articleId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("삭제 성공."));
        then(articleService).should().deleteArticle(any(), any());
    }

    @DisplayName("게시글 삭제 - 실패")
    @Test
    void givenArticleIdAndUserId_whenDeleteArticle_thenReturns4xx() throws Exception {
        // Given
        Long articleId = 1L;
        UserMismatchException exception = new UserMismatchException();
        willThrow(exception).given(articleService).deleteArticle(any(), any());

        // When & Then
        mvc.perform(delete("/v1/articles/" + articleId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(articleService).should().deleteArticle(any(), any());
    }

}