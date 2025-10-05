package com.sdoaolo.office_reservation.app.employee.repository;

import com.sdoaolo.office_reservation.app.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeNumber(Integer employeeNumber);
    
    @Query("SELECT MAX(e.employeeNumber) FROM Employee e")
    Optional<Integer> findMaxEmployeeNumber();
}
