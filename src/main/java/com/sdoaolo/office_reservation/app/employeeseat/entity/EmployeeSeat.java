package com.sdoaolo.office_reservation.app.employeeseat.entity;

import com.sdoaolo.office_reservation.app.employee.entity.Employee;
import com.sdoaolo.office_reservation.app.seat.entity.Seat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "employee_seat",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employee_id", "seat_id", "reserve_date"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 직원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // 좌석
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid; // 예약 유효 여부 (1=예약, 0=취소)

    @Column(name = "reserve_date", nullable = false)
    private LocalDate reserveDate; // 예약 날짜
}
