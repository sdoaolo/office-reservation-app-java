package com.sdoaolo.office_reservation.app.employee.service;

import com.sdoaolo.office_reservation.app.employee.dto.EmployeeCreateRequestDto;
import com.sdoaolo.office_reservation.app.employee.dto.EmployeeResponseDto;
import com.sdoaolo.office_reservation.app.employee.entity.Employee;
import com.sdoaolo.office_reservation.app.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeCreateRequestDto request) {
        // 직원 번호 자동 생성 (1~150 범위)
        Integer nextEmployeeNumber = employeeRepository.findMaxEmployeeNumber()
                .map(max -> max + 1)
                .orElse(1);

        if (nextEmployeeNumber > 150) {
            throw new IllegalArgumentException("직원 수가 최대치(150명)에 도달했습니다.");
        }

        Employee employee = Employee.builder()
                .employeeNumber(nextEmployeeNumber)
                .name(request.getName())
                .currentWorkType(request.getCurrentWorkType())
                .createdDate(request.getCreatedDate() != null ? request.getCreatedDate() : java.time.LocalDate.now())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeResponseDto.builder()
                .id(savedEmployee.getId())
                .employeeNumber(savedEmployee.getEmployeeNumber())
                .name(savedEmployee.getName())
                .currentWorkType(savedEmployee.getCurrentWorkType())
                .createdDate(savedEmployee.getCreatedDate())
                .build();
    }

    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(employee -> EmployeeResponseDto.builder()
                        .id(employee.getId())
                        .employeeNumber(employee.getEmployeeNumber())
                        .name(employee.getName())
                        .currentWorkType(employee.getCurrentWorkType())
                        .createdDate(employee.getCreatedDate())
                        .build());
    }

    public EmployeeResponseDto getEmployeeByNumber(Integer employeeNumber) {
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new IllegalArgumentException("Employee Not Found"));
        
        return EmployeeResponseDto.builder()
                .id(employee.getId())
                .employeeNumber(employee.getEmployeeNumber())
                .name(employee.getName())
                .currentWorkType(employee.getCurrentWorkType())
                .createdDate(employee.getCreatedDate())
                .build();
    }
}
