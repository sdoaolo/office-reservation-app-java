package com.sdoaolo.office_reservation.app.common.exception;

import com.sdoaolo.office_reservation.app.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.<Void>builder()
                        .status("BAD_REQUEST")
                        .message("잘못된 요청입니다.")
                        .code(400)
                        .isSuccess(false)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(ApiResponse.<Void>builder()
                        .status("INTERNAL_SERVER_ERROR")
                        .message("서버 오류가 발생했습니다.")
                        .code(500)
                        .isSuccess(false)
                        .build());
    }
}
