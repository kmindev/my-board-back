package com.back.controler;

import com.back.config.JsonDataEncoder;
import com.back.config.SecurityConfig;
import com.back.controler.dto.request.NewCommentRequest;
import com.back.exception.ArticleNotFoundException;
import com.back.exception.CommentNotFoundException;
import com.back.service.CommentService;
import com.back.service.dto.ArticleWithCommentsWithHashtagsDto;
import com.back.service.dto.NewCommentRequestDto;
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

import static com.back.controler.dto.request.NewCommentRequestFactory.createNewCommentRequest;
import static com.back.service.dto.ArticleWithCommentsWithHashtagsDtoFactory.createArticleWithCommentsWithHashtagsDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("컨트롤러 - 댓글")
@Import({JsonDataEncoder.class})
@WebMvcTest(
        controllers = CommentController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
        }
)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonDataEncoder jsonDataEncoder;

    @MockitoBean
    private CommentService commentService;

    @DisplayName("댓글 등록 요청 - 성공")
    @Test
    void givenNewCommentRequest_whenNewComment_thenReturns200() throws Exception {
        // Given
        NewCommentRequest request = createNewCommentRequest();
        ArticleWithCommentsWithHashtagsDto dto = createArticleWithCommentsWithHashtagsDto();
        given(commentService.newComment(any(NewCommentRequestDto.class))).willReturn(dto);

        // When & Then
        mvc.perform(post("/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message").isEmpty());
        then(commentService).should().newComment(any(NewCommentRequestDto.class));
    }

    @DisplayName("댓글 등록 요청 - 실페")
    @Test
    void givenNewCommentRequest_whenNewComment_thenReturns4xx() throws Exception {
        // Given
        NewCommentRequest request = createNewCommentRequest();
        ArticleNotFoundException exception = new ArticleNotFoundException();
        given(commentService.newComment(any(NewCommentRequestDto.class))).willThrow(exception);

        // When & Then
        mvc.perform(post("/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(commentService).should().newComment(any(NewCommentRequestDto.class));
    }

    @DisplayName("댓글 삭제 요청 - 성공")
    @Test
    void givenCommentId_whenDeleteComment_thenReturns200() throws Exception {
        // Given
        Long commentId = 1L;
        willDoNothing().given(commentService).deleteComment(any(), any());

        // When & Then
        mvc.perform(delete("/v1/comments/" + commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("삭제 성공."));
        then(commentService).should().deleteComment(any(), any());
    }

    @DisplayName("댓글 삭제 요청 - 실패")
    @Test
    void givenCommentId_whenDeleteComment_thenReturns4xx() throws Exception {
        // Given
        Long commentId = 1L;
        CommentNotFoundException exception = new CommentNotFoundException();
        willThrow(exception).given(commentService).deleteComment(any(), any());

        // When & Then
        mvc.perform(delete("/v1/comments/" + commentId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
        then(commentService).should().deleteComment(any(), any());
    }


}
