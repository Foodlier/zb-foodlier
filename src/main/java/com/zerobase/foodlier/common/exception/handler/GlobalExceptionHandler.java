package com.zerobase.foodlier.common.exception.handler;

import com.zerobase.foodlier.common.exception.dto.ErrorResponse;
import com.zerobase.foodlier.common.exception.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<List<ErrorResponse>> inputValidationExceptionHandler(BindingResult results) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(results.getFieldErrors()
                        .stream()
                        .map(e -> new ErrorResponse(e.getCode(), e.getDefaultMessage()))
                        .collect(Collectors.toList()));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> CustomExceptionHandler(BaseException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorCode(e.getErrorCode().name())
                        .description(e.getDescription())
                        .build());
    }

}
