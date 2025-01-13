package com.back.exception;

public class UnexpectedSearchTypeException extends ApplicationException{
    public UnexpectedSearchTypeException() {
        super(ErrorCode.UNEXPECTED_SEARCH_TYPE);
    }
}
