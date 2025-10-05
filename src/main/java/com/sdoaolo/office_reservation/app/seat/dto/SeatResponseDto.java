package com.sdoaolo.office_reservation.app.seat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponseDto {
    private Long id;
    private Integer seatNumber;
    private String seatLocation;
    private LocalDate createdDate;
}
