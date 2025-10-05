package com.sdoaolo.office_reservation.app.employeeseat.controller;

import com.sdoaolo.office_reservation.app.common.dto.ApiResponse;
import com.sdoaolo.office_reservation.app.employeeseat.dto.ReservationRequestDto;
import com.sdoaolo.office_reservation.app.employeeseat.dto.ReservationResponseDto;
import com.sdoaolo.office_reservation.app.employeeseat.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> createReservation(@Valid @RequestBody ReservationRequestDto request) {
        ReservationResponseDto response = reservationService.createReservation(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ReservationResponseDto>builder()
                        .status("SUCCESS")
                        .message("예약이 완료되었습니다.")
                        .code(201)
                        .isSuccess(true)
                        .data(response)
                        .build());
    }

    @DeleteMapping("/reservations")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> cancelReservation(@Valid @RequestBody ReservationRequestDto request) {
        ReservationResponseDto response = reservationService.cancelReservation(request);
        
        return ResponseEntity.ok(
                ApiResponse.<ReservationResponseDto>builder()
                        .status("SUCCESS")
                        .message("예약이 취소되었습니다.")
                        .code(200)
                        .isSuccess(true)
                        .data(response)
                        .build());
    }
}
