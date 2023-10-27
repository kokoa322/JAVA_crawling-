package com.example.test.crall.customException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    FAILED_MESSAGE(500, "E-FAI500","서버에서 오류가 발생하였습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
