package com.sdoaolo.office_reservation.app.employeeseat.repository;

import com.sdoaolo.office_reservation.app.employee.entity.Employee;
import com.sdoaolo.office_reservation.app.employeeseat.entity.EmployeeSeat;
import com.sdoaolo.office_reservation.app.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeSeatRepository extends JpaRepository<EmployeeSeat, Long> {
    boolean existsByEmployeeAndReserveDateAndIsValidTrue(Employee employee, LocalDate reserveDate);
    boolean existsBySeatAndReserveDateAndIsValidTrue(Seat seat, LocalDate reserveDate);
    Optional<EmployeeSeat> findByEmployeeAndSeatAndReserveDateAndIsValidTrue(Employee employee, Seat seat, LocalDate reserveDate);
}
