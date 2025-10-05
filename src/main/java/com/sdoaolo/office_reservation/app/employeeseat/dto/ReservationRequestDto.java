package com.sdoaolo.office_reservation.app.employeeseat.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDto {
    @NotNull(message = "직원 번호는 필수입니다")
    @Min(value = 1, message = "직원 번호는 1 이상이어야 합니다")
    @Max(value = 150, message = "직원 번호는 150 이하여야 합니다")
    private Integer employeeNumber;
    
    @NotNull(message = "좌석 번호는 필수입니다")
    @Min(value = 1, message = "좌석 번호는 1 이상이어야 합니다")
    @Max(value = 100, message = "좌석 번호는 100 이하여야 합니다")
    private Integer seatNumber;
}
