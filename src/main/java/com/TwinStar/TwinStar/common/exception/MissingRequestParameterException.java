package com.TwinStar.TwinStar.common.exception;

public class MissingRequestParameterException extends RuntimeException{
    public MissingRequestParameterException(String message) {
        super(message);
    }
}
