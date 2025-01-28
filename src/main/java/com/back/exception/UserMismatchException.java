package com.back.exception;

public class UserMismatchException extends ApplicationException {
    public UserMismatchException() {
        super(ErrorCode.USER_MISMATCH);
    }
}
