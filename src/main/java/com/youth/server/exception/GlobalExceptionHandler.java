package com.youth.server.exception;

import com.youth.server.dto.RestEntity;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "데이터 무결성 위반:\n " + ex.getRootCause().getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NotFoundException.class, WrongInputException.class})
    public ResponseEntity<Map<String, Object>> handleNotFoundException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SignatureException.class)
    public RestEntity handleSingatureException(SignatureException ex) {
        return RestEntity.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message("JWT 서명이 일치하지 않습니다.")
                .put("description","서버가 새로 시작되기 이전에 생성된 토큰입니다.")
                .build();
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public RestEntity handlePermissionDeniedException(PermissionDeniedException ex) {
        return RestEntity.builder()
                .status(HttpStatus.FORBIDDEN)
                .message("권한이 없습니다.")
                .put("description", ex.getMessage())
                .build();
    }
}
