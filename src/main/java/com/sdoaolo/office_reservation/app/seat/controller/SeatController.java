package com.sdoaolo.office_reservation.app.seat.controller;

import com.sdoaolo.office_reservation.app.common.dto.ApiResponse;
import com.sdoaolo.office_reservation.app.seat.dto.SeatCreateRequestDto;
import com.sdoaolo.office_reservation.app.seat.dto.SeatResponseDto;
import com.sdoaolo.office_reservation.app.seat.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<ApiResponse<SeatResponseDto>> createSeat(@Valid @RequestBody SeatCreateRequestDto request) {
        SeatResponseDto response = seatService.createSeat(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SeatResponseDto>builder()
                        .status("SUCCESS")
                        .message("좌석이 등록되었습니다.")
                        .code(201)
                        .isSuccess(true)
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SeatResponseDto>>> getAllSeats(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<SeatResponseDto> seats = seatService.getAllSeats(pageable);
        
        return ResponseEntity.ok(
                ApiResponse.<Page<SeatResponseDto>>builder()
                        .status("SUCCESS")
                        .message("좌석 목록을 조회했습니다.")
                        .code(200)
                        .isSuccess(true)
                        .data(seats)
                        .build());
    }

    @GetMapping("/{seatNumber}")
    public ResponseEntity<ApiResponse<SeatResponseDto>> getSeatByNumber(@PathVariable Integer seatNumber) {
        SeatResponseDto response = seatService.getSeatByNumber(seatNumber);
        
        return ResponseEntity.ok(
                ApiResponse.<SeatResponseDto>builder()
                        .status("SUCCESS")
                        .message("좌석 정보를 조회했습니다.")
                        .code(200)
                        .isSuccess(true)
                        .data(response)
                        .build());
    }
}
