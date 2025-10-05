package com.sdoaolo.office_reservation.app.common;

import com.sdoaolo.office_reservation.app.employee.dto.EmployeeCreateRequestDto;
import com.sdoaolo.office_reservation.app.employee.entity.Employee;
import com.sdoaolo.office_reservation.app.employee.entity.WorkType;
import com.sdoaolo.office_reservation.app.employeeseat.dto.ReservationRequestDto;
import com.sdoaolo.office_reservation.app.seat.dto.SeatCreateRequestDto;
import com.sdoaolo.office_reservation.app.seat.entity.Seat;

import java.time.LocalDate;

public class TestDataFactory {

    public static Employee createEmployee() {
        return Employee.builder()
                .employeeNumber(1)
                .name("김철수")
                .currentWorkType(WorkType.OFFICE)
                .createdDate(LocalDate.now())
                .build();
    }

    public static Employee createEmployeeWithNumber(Integer employeeNumber) {
        return Employee.builder()
                .employeeNumber(employeeNumber)
                .name("김철수")
                .currentWorkType(WorkType.OFFICE)
                .createdDate(LocalDate.now())
                .build();
    }

    public static EmployeeCreateRequestDto createEmployeeRequest() {
        return EmployeeCreateRequestDto.builder()
                .name("김철수")
                .currentWorkType(WorkType.OFFICE)
                .createdDate(LocalDate.now())
                .build();
    }

    public static Seat createSeat() {
        return Seat.builder()
                .seatNumber(1)
                .seatLocation("롯데월드타워 27F Room A")
                .createdDate(LocalDate.now())
                .build();
    }

    public static Seat createSeatWithNumber(Integer seatNumber) {
        return Seat.builder()
                .seatNumber(seatNumber)
                .seatLocation("롯데월드타워 27F Room A")
                .createdDate(LocalDate.now())
                .build();
    }

    public static SeatCreateRequestDto createSeatRequest() {
        return SeatCreateRequestDto.builder()
                .seatNumber(1)
                .seatLocation("롯데월드타워 27F Room A")
                .createdDate(LocalDate.now())
                .build();
    }

    public static ReservationRequestDto createReservationRequest() {
        return ReservationRequestDto.builder()
                .employeeNumber(1)
                .seatNumber(1)
                .build();
    }

    public static ReservationRequestDto createReservationRequest(Integer employeeNumber, Integer seatNumber) {
        return ReservationRequestDto.builder()
                .employeeNumber(employeeNumber)
                .seatNumber(seatNumber)
                .build();
    }
}
