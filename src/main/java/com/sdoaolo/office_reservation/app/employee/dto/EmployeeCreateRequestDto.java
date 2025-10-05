package com.sdoaolo.office_reservation.app.employee.dto;

import com.sdoaolo.office_reservation.app.employee.entity.WorkType;
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
public class EmployeeCreateRequestDto {
    @NotBlank(message = "이름은 필수입니다")
    private String name;
    
    @NotNull(message = "근무 형태는 필수입니다")
    private WorkType currentWorkType;
    
    private LocalDate createdDate;
}
