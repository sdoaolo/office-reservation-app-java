package com.sdoaolo.office_reservation.app.employee.dto;

import com.sdoaolo.office_reservation.app.employee.entity.WorkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDto {
    private Long id;
    private Integer employeeNumber;
    private String name;
    private WorkType currentWorkType;
    private LocalDate createdDate;
}
