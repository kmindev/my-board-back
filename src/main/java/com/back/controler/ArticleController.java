package com.back.controler;

import com.back.controler.dto.reponse.ApiResponse;
import com.back.controler.dto.reponse.ArticleResponse;
import com.back.controler.dto.reponse.SearchArticleResponse;
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
    public ResponseEntity<ApiResponse<ArticleResponse>> newArticle(
            @RequestBody @Valid NewArticleRequest request,
            @AuthenticationPrincipal BoardUserDetails boardUserDetails
    ) {
        return ResponseEntity.ok().body(
                ApiResponse.okWithData(
                        articleService.newArticle(request.toDto(boardUserDetails.userId())).toResponse()
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

}
