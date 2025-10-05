package com.sdoaolo.office_reservation.app.seat.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatCreateRequestDto {
    @NotNull(message = "좌석 번호는 필수입니다")
    @Min(value = 1, message = "좌석 번호는 1 이상이어야 합니다")
    @Max(value = 100, message = "좌석 번호는 100 이하여야 합니다")
    private Integer seatNumber;
    
    @NotBlank(message = "좌석 위치는 필수입니다")
    private String seatLocation;
    
    private LocalDate createdDate;
}
