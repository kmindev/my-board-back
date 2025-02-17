package com.back.service;

import com.back.domain.Article;
import com.back.domain.Comment;
import com.back.domain.UserAccount;
import com.back.exception.CommentNotFoundException;
import com.back.exception.UserMismatchException;
import com.back.repository.CommentRepository;
import com.back.service.dto.ArticleWithCommentsWithHashtagsDto;
import com.back.service.dto.NewCommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final ArticleService articleService;
    private final UserAccountService userAccountService;
    private final CommentRepository commentRepository;

    public ArticleWithCommentsWithHashtagsDto newComment(NewCommentRequestDto dto) {
        Article findArticle = articleService.findArticle(dto.articleId());
        UserAccount findUserAccount = userAccountService.getUserAccount(dto.userId());
        Comment newComment = Comment.newComment(findArticle, findUserAccount, dto.content());
        commentRepository.save(newComment);

        return ArticleWithCommentsWithHashtagsDto.from(findArticle);
    }

    public void deleteComment(Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        UserAccount userAccount = userAccountService.getUserAccount(userId);
        if (!comment.getUserAccount().equals(userAccount)) {
            throw new UserMismatchException();
        }

        commentRepository.deleteById(commentId);
    }

}
