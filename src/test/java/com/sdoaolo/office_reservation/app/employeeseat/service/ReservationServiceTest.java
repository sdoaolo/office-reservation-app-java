package com.sdoaolo.office_reservation.app.employeeseat.service;

import com.sdoaolo.office_reservation.app.common.TestDataFactory;
import com.sdoaolo.office_reservation.app.employee.entity.Employee;
import com.sdoaolo.office_reservation.app.employee.entity.WorkType;
import com.sdoaolo.office_reservation.app.employee.repository.EmployeeRepository;
import com.sdoaolo.office_reservation.app.employeeseat.dto.ReservationRequestDto;
import com.sdoaolo.office_reservation.app.employeeseat.dto.ReservationResponseDto;
import com.sdoaolo.office_reservation.app.employeeseat.entity.EmployeeSeat;
import com.sdoaolo.office_reservation.app.employeeseat.repository.EmployeeSeatRepository;
import com.sdoaolo.office_reservation.app.seat.entity.Seat;
import com.sdoaolo.office_reservation.app.seat.repository.SeatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("예약 서비스 테스트")
class ReservationServiceTest {

    @Mock
    private EmployeeSeatRepository employeeSeatRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 생성 성공")
    void createReservation_Success() {
        // given
        ReservationRequestDto request = TestDataFactory.createReservationRequest();
        Employee employee = TestDataFactory.createEmployee();
        employee.setId(1L);
        Seat seat = TestDataFactory.createSeat();
        seat.setId(1L);
        EmployeeSeat savedReservation = EmployeeSeat.builder()
                .id(1L)
                .employee(employee)
                .seat(seat)
                .isValid(true)
                .reserveDate(LocalDate.now())
                .build();

        when(employeeRepository.findByEmployeeNumber(1)).thenReturn(Optional.of(employee));
        when(seatRepository.findBySeatNumber(1)).thenReturn(Optional.of(seat));
        when(employeeSeatRepository.existsByEmployeeAndReserveDateAndIsValidTrue(any(), any())).thenReturn(false);
        when(employeeSeatRepository.existsBySeatAndReserveDateAndIsValidTrue(any(), any())).thenReturn(false);
        when(employeeSeatRepository.save(any(EmployeeSeat.class))).thenReturn(savedReservation);

        // when
        ReservationResponseDto result = reservationService.createReservation(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeId()).isEqualTo(1L);
        assertThat(result.getSeatId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("예약 생성 실패 - 직원 없음")
    void createReservation_EmployeeNotFound() {
        // given
        ReservationRequestDto request = TestDataFactory.createReservationRequest();
        when(employeeRepository.findByEmployeeNumber(1)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Employee Not Found");
    }

    @Test
    @DisplayName("예약 생성 실패 - 좌석 없음")
    void createReservation_SeatNotFound() {
        // given
        ReservationRequestDto request = TestDataFactory.createReservationRequest();
        Employee employee = TestDataFactory.createEmployee();
        employee.setId(1L);

        when(employeeRepository.findByEmployeeNumber(1)).thenReturn(Optional.of(employee));
        when(seatRepository.findBySeatNumber(1)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Seat Not Found");
    }

    @Test
    @DisplayName("예약 생성 실패 - 이미 예약한 직원")
    void createReservation_EmployeeAlreadyReserved() {
        // given
        ReservationRequestDto request = TestDataFactory.createReservationRequest();
        Employee employee = TestDataFactory.createEmployee();
        employee.setId(1L);
        Seat seat = TestDataFactory.createSeat();
        seat.setId(1L);

        when(employeeRepository.findByEmployeeNumber(1)).thenReturn(Optional.of(employee));
        when(seatRepository.findBySeatNumber(1)).thenReturn(Optional.of(seat));
        when(employeeSeatRepository.existsByEmployeeAndReserveDateAndIsValidTrue(any(), any())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This user has already completed a reservation");
    }

    @Test
    @DisplayName("예약 생성 실패 - 이미 예약된 좌석")
    void createReservation_SeatAlreadyReserved() {
        // given
        ReservationRequestDto request = TestDataFactory.createReservationRequest();
        Employee employee = TestDataFactory.createEmployee();
        employee.setId(1L);
        Seat seat = TestDataFactory.createSeat();
        seat.setId(1L);

        when(employeeRepository.findByEmployeeNumber(1)).thenReturn(Optional.of(employee));
        when(seatRepository.findBySeatNumber(1)).thenReturn(Optional.of(seat));
        when(employeeSeatRepository.existsByEmployeeAndReserveDateAndIsValidTrue(any(), any())).thenReturn(false);
        when(employeeSeatRepository.existsBySeatAndReserveDateAndIsValidTrue(any(), any())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This seat is already reserved. Please choose a different seat");
    }

    @Test
    @DisplayName("예약 취소 성공")
    void cancelReservation_Success() {
        // given
        ReservationRequestDto request = TestDataFactory.createReservationRequest();
        Employee employee = TestDataFactory.createEmployee();
        employee.setId(1L);
        Seat seat = TestDataFactory.createSeat();
        seat.setId(1L);
        EmployeeSeat reservation = EmployeeSeat.builder()
                .id(1L)
                .employee(employee)
                .seat(seat)
                .isValid(true)
                .reserveDate(LocalDate.now())
                .build();

        when(employeeRepository.findByEmployeeNumber(1)).thenReturn(Optional.of(employee));
        when(seatRepository.findBySeatNumber(1)).thenReturn(Optional.of(seat));
        when(employeeSeatRepository.findByEmployeeAndSeatAndReserveDateAndIsValidTrue(any(), any(), any()))
                .thenReturn(Optional.of(reservation));
        when(employeeSeatRepository.save(any(EmployeeSeat.class))).thenReturn(reservation);

        // when
        ReservationResponseDto result = reservationService.cancelReservation(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeId()).isEqualTo(1L);
        assertThat(result.getSeatId()).isEqualTo(1L);
        assertThat(reservation.getIsValid()).isFalse();
    }

    @Test
    @DisplayName("예약 취소 실패 - 예약 없음")
    void cancelReservation_ReservationNotFound() {
        // given
        ReservationRequestDto request = TestDataFactory.createReservationRequest();
        Employee employee = TestDataFactory.createEmployee();
        employee.setId(1L);
        Seat seat = TestDataFactory.createSeat();
        seat.setId(1L);

        when(employeeRepository.findByEmployeeNumber(1)).thenReturn(Optional.of(employee));
        when(seatRepository.findBySeatNumber(1)).thenReturn(Optional.of(seat));
        when(employeeSeatRepository.findByEmployeeAndSeatAndReserveDateAndIsValidTrue(any(), any(), any()))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.cancelReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reservation Not Found");
    }
}
