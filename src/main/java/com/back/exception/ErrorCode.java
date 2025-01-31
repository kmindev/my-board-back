package com.back.exception;

import com.back.domain.constant.SearchType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum ErrorCode {

    // Article - Comment
    UNEXPECTED_SEARCH_TYPE(HttpStatus.BAD_REQUEST, "잘못된 검색 타입입니다. 가능한 검색 타입: " +
            Arrays.stream(SearchType.values()).map(SearchType::getTypeName).toList()
    ),
    ARTICLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    USER_MISMATCH(HttpStatus.BAD_REQUEST, "작성자가 아닙니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "댓글을 찾을 수 없습니다."),

    // UserAccount
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."),

    //500 error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
