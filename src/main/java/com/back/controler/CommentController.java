package com.back.controler;

import com.back.controler.dto.reponse.ApiResponse;
import com.back.controler.dto.reponse.ArticleDetailsResponse;
import com.back.controler.dto.request.NewCommentRequest;
import com.back.secuirty.BoardUserDetails;
import com.back.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleDetailsResponse>> newComment(
            @RequestBody @Valid NewCommentRequest request,
            @AuthenticationPrincipal BoardUserDetails boardUserDetails
    ) {
        return ResponseEntity.ok().body(
                ApiResponse.okWithData(
                        ArticleDetailsResponse.from(
                                commentService.newComment(request.toDto(boardUserDetails.toDto()))
                        )
                )
        );
    }

}
