package com.jungle.studybbitback.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //==== 400 ================================================================================//

    // IllegalArgumentException 전용 핸들러 : 잘못된 인자가 전달될 때
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        logException("IllegalArgumentException", ex);
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // @Valid 유효성 검사 실패에 대한 핸들러 추가
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logException("MethodArgumentNotValidException", ex);
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI());
        errorResponse.put("details", ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                )));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //==== 401 ================================================================================//

    // 잘못된 자격 증명을 제공했을 때
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        logException("BadCredentialsException", ex);
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // spring security에서 인증실패 시
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        logException("AuthenticationException", ex);
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    //==== 403 ================================================================================//

    // spring security에서 인가실패 시
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        logException("AccessDeniedException", ex);
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    //==== 404 ================================================================================//

    // JPA에서 특정 엔티티를 찾지 못했을 때
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        logException("EntityNotFoundException", ex);
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 매핑된 핸들러를 찾지 못했을 때
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        logException("NoHandlerFoundException", ex);
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //==== 409 ================================================================================//

    // 데이터베이스 무결성 제약 조건 위반 시
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        logException("DataIntegrityViolationException", ex);
        Map<String, Object> errorResponse = createErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    //==== 500 ================================================================================//

    // 전체 예외 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception occurred: {}", ex.getMessage(), ex); // 로그 추가
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

    private void logException(String exceptionType, Exception ex) {
        logger.error("{} occurred: {}", exceptionType, ex.getMessage(), ex);
    }
}
