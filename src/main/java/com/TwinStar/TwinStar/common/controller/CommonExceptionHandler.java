package com.TwinStar.TwinStar.common.controller;

import com.TwinStar.TwinStar.common.dto.CommonErrorDto;
import com.TwinStar.TwinStar.common.exception.MissingRequestParameterException;
import com.TwinStar.TwinStar.common.exception.PrivateAccountException;
import com.TwinStar.TwinStar.common.exception.SuspendedAccountException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFound(EntityNotFoundException e) {
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> illegalStateException(IllegalStateException e) {
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgument(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SuspendedAccountException.class)
    public ResponseEntity<?> suspendedAccountException(SuspendedAccountException e){
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PrivateAccountException.class)
    public ResponseEntity<?> privateAccountException(PrivateAccountException e){
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

//   변경할 공개 범위 데이터가 없을 경우
    @ExceptionHandler(MissingRequestParameterException.class)
    public ResponseEntity<?> missingRequestParameter(MissingRequestParameterException e){
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException e){
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.FORBIDDEN.value(), e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
