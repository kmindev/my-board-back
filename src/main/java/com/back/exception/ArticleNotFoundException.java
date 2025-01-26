package com.back.exception;

public class ArticleNotFoundException extends ApplicationException{
    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
