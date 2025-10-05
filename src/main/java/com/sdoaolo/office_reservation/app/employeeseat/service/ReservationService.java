package com.sdoaolo.office_reservation.app.employeeseat.service;

import com.sdoaolo.office_reservation.app.employee.entity.Employee;
import com.sdoaolo.office_reservation.app.employee.repository.EmployeeRepository;
import com.sdoaolo.office_reservation.app.employeeseat.dto.ReservationRequestDto;
import com.sdoaolo.office_reservation.app.employeeseat.dto.ReservationResponseDto;
import com.sdoaolo.office_reservation.app.employeeseat.entity.EmployeeSeat;
import com.sdoaolo.office_reservation.app.employeeseat.repository.EmployeeSeatRepository;
import com.sdoaolo.office_reservation.app.seat.entity.Seat;
import com.sdoaolo.office_reservation.app.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final EmployeeSeatRepository employeeSeatRepository;
    private final EmployeeRepository employeeRepository;
    private final SeatRepository seatRepository;

    public ReservationResponseDto createReservation(ReservationRequestDto request) {
        // 직원 존재 확인
        Employee employee = employeeRepository.findByEmployeeNumber(request.getEmployeeNumber())
                .orElseThrow(() -> new IllegalArgumentException("Employee Not Found"));

        // 좌석 존재 확인
        Seat seat = seatRepository.findBySeatNumber(request.getSeatNumber())
                .orElseThrow(() -> new IllegalArgumentException("Seat Not Found"));

        LocalDate today = LocalDate.now();

        // 해당 직원이 오늘 이미 예약했는지 확인
        boolean hasExistingReservation = employeeSeatRepository.existsByEmployeeAndReserveDateAndIsValidTrue(employee, today);
        if (hasExistingReservation) {
            throw new IllegalStateException("This user has already completed a reservation");
        }

        // 해당 좌석이 오늘 이미 예약되었는지 확인
        boolean seatAlreadyReserved = employeeSeatRepository.existsBySeatAndReserveDateAndIsValidTrue(seat, today);
        if (seatAlreadyReserved) {
            throw new IllegalStateException("This seat is already reserved. Please choose a different seat");
        }

        // 예약 생성
        EmployeeSeat reservation = EmployeeSeat.builder()
                .employee(employee)
                .seat(seat)
                .isValid(true)
                .reserveDate(today)
                .build();

        EmployeeSeat savedReservation = employeeSeatRepository.save(reservation);

        return ReservationResponseDto.builder()
                .employeeId(savedReservation.getEmployee().getId())
                .seatId(savedReservation.getSeat().getId())
                .build();
    }

    public ReservationResponseDto cancelReservation(ReservationRequestDto request) {
        // 직원 존재 확인
        Employee employee = employeeRepository.findByEmployeeNumber(request.getEmployeeNumber())
                .orElseThrow(() -> new IllegalArgumentException("Employee Not Found"));

        // 좌석 존재 확인
        Seat seat = seatRepository.findBySeatNumber(request.getSeatNumber())
                .orElseThrow(() -> new IllegalArgumentException("Seat Not Found"));

        LocalDate today = LocalDate.now();

        // 예약 존재 확인
        EmployeeSeat reservation = employeeSeatRepository.findByEmployeeAndSeatAndReserveDateAndIsValidTrue(employee, seat, today)
                .orElseThrow(() -> new IllegalArgumentException("Reservation Not Found"));

        // 예약 취소 (isValid를 false로 변경)
        reservation.setIsValid(false);
        employeeSeatRepository.save(reservation);

        return ReservationResponseDto.builder()
                .employeeId(reservation.getEmployee().getId())
                .seatId(reservation.getSeat().getId())
                .build();
    }
}
