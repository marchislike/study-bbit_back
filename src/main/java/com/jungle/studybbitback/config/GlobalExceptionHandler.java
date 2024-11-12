package com.jungle.studybbitback.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException 전용 핸들러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // @Valid 유효성 검사 실패에 대한 핸들러 추가
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI());
        errorResponse.put("details", ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 전체 예외 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, HttpServletRequest request) {
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 공통 에러 응답 생성 메서드
    private Map<String, Object> createErrorResponse(HttpStatus status, String message, String path) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message != null ? message : "Unexpected error occurred.");
        errorResponse.put("path", path);
        return errorResponse;
    }
}
