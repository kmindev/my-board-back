package com.back.controler;

import com.back.config.JsonDataEncoder;
import com.back.config.SecurityConfig;
import com.back.controler.dto.request.NewArticleRequest;
import com.back.exception.UserNotFoundException;
import com.back.service.ArticleService;
import com.back.service.dto.ArticleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.back.controler.dto.request.NewArticleRequestFactory.createDefaultNewArticleRequest;
import static com.back.service.dto.ArticleDtoFactory.createArticleDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("컨트롤러 - 게시글")
@Import({JsonDataEncoder.class})
@WebMvcTest(
        controllers = ArticleController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
        }
)
class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonDataEncoder jsonDataEncoder;

    @MockitoBean
    private ArticleService articleService;

    @DisplayName("게시글 생성 요청 - 성공")
    @Test
    void givenNewArticleRequest_whenNewArticle_thenReturns200() throws Exception {
        // Given
        NewArticleRequest request = createDefaultNewArticleRequest();
        ArticleDto articleDto = createArticleDto();
        given(articleService.newArticle(any(ArticleDto.class))).willReturn(articleDto);

        // When & Then
        mvc.perform(post("/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(articleService).should().newArticle(any(ArticleDto.class));
    }

    @DisplayName("게시글 생성 요청 - 실패")
    @Test
    void givenNewArticleRequest_whenNewArticle_thenReturns4xx() throws Exception {
        // Given
        NewArticleRequest request = createDefaultNewArticleRequest();
        given(articleService.newArticle(any(ArticleDto.class))).willThrow(new UserNotFoundException());

        // When & Then
        mvc.perform(post("/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(new UserNotFoundException().getMessage()));
        then(articleService).should().newArticle(any(ArticleDto.class));
    }

}