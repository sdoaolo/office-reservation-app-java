package com.sdoaolo.office_reservation.app.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdoaolo.office_reservation.app.common.TestDataFactory;
import com.sdoaolo.office_reservation.app.employee.dto.EmployeeCreateRequestDto;
import com.sdoaolo.office_reservation.app.employee.dto.EmployeeResponseDto;
import com.sdoaolo.office_reservation.app.employee.entity.WorkType;
import com.sdoaolo.office_reservation.app.employee.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@DisplayName("직원 컨트롤러 테스트")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("직원 등록 성공")
    void createEmployee_Success() throws Exception {
        // given
        EmployeeCreateRequestDto request = TestDataFactory.createEmployeeRequest();
        EmployeeResponseDto response = EmployeeResponseDto.builder()
                .id(1L)
                .employeeNumber(1)
                .name("김철수")
                .currentWorkType(WorkType.OFFICE)
                .createdDate(LocalDate.now())
                .build();

        when(employeeService.createEmployee(any(EmployeeCreateRequestDto.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("직원이 등록되었습니다."))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.employeeNumber").value(1))
                .andExpect(jsonPath("$.data.name").value("김철수"));
    }

    @Test
    @DisplayName("직원 목록 조회 성공")
    void getAllEmployees_Success() throws Exception {
        // given
        EmployeeResponseDto employee = EmployeeResponseDto.builder()
                .id(1L)
                .employeeNumber(1)
                .name("김철수")
                .currentWorkType(WorkType.OFFICE)
                .createdDate(LocalDate.now())
                .build();

        Page<EmployeeResponseDto> page = new PageImpl<>(List.of(employee), PageRequest.of(0, 20), 1);
        when(employeeService.getAllEmployees(any()))
                .thenReturn(page);

        // when & then
        mockMvc.perform(get("/api/v1/employees")
                        .param("page", "1")
                        .param("size", "20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("직원 목록을 조회했습니다."))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].name").value("김철수"));
    }

    @Test
    @DisplayName("직원 상세 조회 성공")
    void getEmployeeByNumber_Success() throws Exception {
        // given
        EmployeeResponseDto response = EmployeeResponseDto.builder()
                .id(1L)
                .employeeNumber(1)
                .name("김철수")
                .currentWorkType(WorkType.OFFICE)
                .createdDate(LocalDate.now())
                .build();

        when(employeeService.getEmployeeByNumber(anyInt()))
                .thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/employees/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("직원 정보를 조회했습니다."))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.employeeNumber").value(1))
                .andExpect(jsonPath("$.data.name").value("김철수"));
    }

    @Test
    @DisplayName("직원 등록 실패 - 유효성 검증 실패")
    void createEmployee_ValidationError() throws Exception {
        // given
        EmployeeCreateRequestDto request = EmployeeCreateRequestDto.builder()
                .name("") // 빈 이름
                .currentWorkType(WorkType.OFFICE)
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
