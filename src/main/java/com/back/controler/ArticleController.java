package com.back.controler;

import com.back.controler.dto.reponse.ApiResponse;
import com.back.controler.dto.reponse.ArticleResponse;
import com.back.controler.dto.request.NewArticleRequest;
import com.back.secuirty.BoardUserDetails;
import com.back.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponse>> newArticle(
            @RequestBody @Valid NewArticleRequest request,
            @AuthenticationPrincipal BoardUserDetails boardUserDetails
    ) {
        return ResponseEntity.ok().body(
                ApiResponse.okWithData(
                        articleService.newArticle(request.toDto(boardUserDetails.userId())).toResponse()
                )
        );
    }

}
