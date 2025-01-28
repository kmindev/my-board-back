package com.back.controler;

import com.back.controler.dto.reponse.ApiResponse;
import com.back.controler.dto.reponse.ArticleDetailsResponse;
import com.back.controler.dto.reponse.ArticleWithHashtagsResponse;
import com.back.controler.dto.reponse.SearchArticleResponse;
import com.back.controler.dto.request.ArticleUpdateRequest;
import com.back.controler.dto.request.NewArticleRequest;
import com.back.domain.constant.SearchType;
import com.back.secuirty.BoardUserDetails;
import com.back.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleWithHashtagsResponse>> newArticle(
            @RequestBody @Valid NewArticleRequest request,
            @AuthenticationPrincipal BoardUserDetails boardUserDetails
    ) {
        return ResponseEntity.ok().body(
                ApiResponse.okWithData(
                        ArticleWithHashtagsResponse.from(
                                articleService.newArticle(request.toDto(boardUserDetails.toDto()))
                        )
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SearchArticleResponse>>> getArticles(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String searchValue,
            @RequestParam(required = false) SearchType searchType
    ) {
        return ResponseEntity.ok().body(
                ApiResponse.okWithData(
                        articleService.searchArticles(pageable, searchValue, searchType)
                                .map(SearchArticleResponse::from)
                )
        );
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleDetailsResponse>> getArticleDetails(@PathVariable Long articleId) {
        return ResponseEntity.ok().body(
                ApiResponse.okWithData(
                        ArticleDetailsResponse.from(articleService.getArticleDetails(articleId))
                )
        );
    }

    @PatchMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleWithHashtagsResponse>> updateArticle(
            @PathVariable Long articleId,
            @RequestBody @Valid ArticleUpdateRequest request,
            @AuthenticationPrincipal BoardUserDetails boardUserDetails
    ) {
        return ResponseEntity.ok().body(
                ApiResponse.okWithData(
                        ArticleWithHashtagsResponse.from(
                                articleService.updateArticle(request.toDto(articleId, boardUserDetails.toDto()))
                        )
                )
        );
    }

}
