package com.sdoaolo.office_reservation.app.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private Long id;   // 내부 PK

    @Column(name = "employee_number", nullable = false, unique = true)
    private Integer employeeNumber;  // 직원 고유 번호 (1~150)

    @Column(nullable = false, length = 20)
    private String name;  // 직원 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "current_work_type", length = 20)
    private WorkType currentWorkType;  // 현재 근무 형태 (오피스, 재택, 휴가, 미출근)

    @Column(name = "created_date")
    private LocalDate createdDate; // 입사일
}
