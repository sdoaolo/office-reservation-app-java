package com.sdoaolo.office_reservation.app.employee.service;

import com.sdoaolo.office_reservation.app.common.TestDataFactory;
import com.sdoaolo.office_reservation.app.employee.dto.EmployeeCreateRequestDto;
import com.sdoaolo.office_reservation.app.employee.dto.EmployeeResponseDto;
import com.sdoaolo.office_reservation.app.employee.entity.Employee;
import com.sdoaolo.office_reservation.app.employee.entity.WorkType;
import com.sdoaolo.office_reservation.app.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("직원 서비스 테스트")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    @DisplayName("직원 등록 성공")
    void createEmployee_Success() {
        // given
        EmployeeCreateRequestDto request = TestDataFactory.createEmployeeRequest();
        Employee savedEmployee = TestDataFactory.createEmployee();
        savedEmployee.setId(1L);

        when(employeeRepository.findMaxEmployeeNumber()).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        // when
        EmployeeResponseDto result = employeeService.createEmployee(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeNumber()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("김철수");
        assertThat(result.getCurrentWorkType()).isEqualTo(WorkType.OFFICE);
    }

    @Test
    @DisplayName("직원 등록 실패 - 최대 직원 수 초과")
    void createEmployee_MaxEmployeeExceeded() {
        // given
        EmployeeCreateRequestDto request = TestDataFactory.createEmployeeRequest();
        when(employeeRepository.findMaxEmployeeNumber()).thenReturn(Optional.of(150));

        // when & then
        assertThatThrownBy(() -> employeeService.createEmployee(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("직원 수가 최대치(150명)에 도달했습니다.");
    }

    @Test
    @DisplayName("직원 목록 조회 성공")
    void getAllEmployees_Success() {
        // given
        Employee employee = TestDataFactory.createEmployee();
        employee.setId(1L);
        Page<Employee> page = new PageImpl<>(List.of(employee), PageRequest.of(0, 20), 1);

        when(employeeRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        // when
        Page<EmployeeResponseDto> result = employeeService.getAllEmployees(PageRequest.of(0, 20));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("김철수");
    }

    @Test
    @DisplayName("직원 상세 조회 성공")
    void getEmployeeByNumber_Success() {
        // given
        Employee employee = TestDataFactory.createEmployee();
        employee.setId(1L);

        when(employeeRepository.findByEmployeeNumber(1)).thenReturn(Optional.of(employee));

        // when
        EmployeeResponseDto result = employeeService.getEmployeeByNumber(1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeNumber()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("김철수");
    }

    @Test
    @DisplayName("직원 상세 조회 실패 - 직원 없음")
    void getEmployeeByNumber_EmployeeNotFound() {
        // given
        when(employeeRepository.findByEmployeeNumber(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> employeeService.getEmployeeByNumber(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Employee Not Found");
    }
}
