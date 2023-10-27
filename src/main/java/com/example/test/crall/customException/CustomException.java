package com.example.test.crall.customException;

public class CustomException extends RuntimeException{

    private ErrorCode errorCode;

    CustomException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
