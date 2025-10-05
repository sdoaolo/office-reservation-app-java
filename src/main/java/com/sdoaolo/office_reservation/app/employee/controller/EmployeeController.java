package com.sdoaolo.office_reservation.app.employee.controller;

import com.sdoaolo.office_reservation.app.common.dto.ApiResponse;
import com.sdoaolo.office_reservation.app.employee.dto.EmployeeCreateRequestDto;
import com.sdoaolo.office_reservation.app.employee.dto.EmployeeResponseDto;
import com.sdoaolo.office_reservation.app.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> createEmployee(@Valid @RequestBody EmployeeCreateRequestDto request) {
        EmployeeResponseDto response = employeeService.createEmployee(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<EmployeeResponseDto>builder()
                        .status("SUCCESS")
                        .message("직원이 등록되었습니다.")
                        .code(201)
                        .isSuccess(true)
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EmployeeResponseDto>>> getAllEmployees(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<EmployeeResponseDto> employees = employeeService.getAllEmployees(pageable);
        
        return ResponseEntity.ok(
                ApiResponse.<Page<EmployeeResponseDto>>builder()
                        .status("SUCCESS")
                        .message("직원 목록을 조회했습니다.")
                        .code(200)
                        .isSuccess(true)
                        .data(employees)
                        .build());
    }

    @GetMapping("/{employeeNumber}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getEmployeeByNumber(@PathVariable Integer employeeNumber) {
        EmployeeResponseDto response = employeeService.getEmployeeByNumber(employeeNumber);
        
        return ResponseEntity.ok(
                ApiResponse.<EmployeeResponseDto>builder()
                        .status("SUCCESS")
                        .message("직원 정보를 조회했습니다.")
                        .code(200)
                        .isSuccess(true)
                        .data(response)
                        .build());
    }
}
