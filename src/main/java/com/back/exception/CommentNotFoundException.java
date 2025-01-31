package com.back.exception;

public class CommentNotFoundException extends ApplicationException {
    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
