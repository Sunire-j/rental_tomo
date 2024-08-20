package com.sunire.rental_tomo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, ""),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, ""),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "")
    ;

    private HttpStatus httpStatus;
    private String message;
}
