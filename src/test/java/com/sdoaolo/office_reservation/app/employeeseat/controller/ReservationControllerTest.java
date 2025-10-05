package com.sdoaolo.office_reservation.app.employeeseat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdoaolo.office_reservation.app.common.TestDataFactory;
import com.sdoaolo.office_reservation.app.employeeseat.dto.ReservationRequestDto;
import com.sdoaolo.office_reservation.app.employeeseat.dto.ReservationResponseDto;
import com.sdoaolo.office_reservation.app.employeeseat.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@DisplayName("예약 컨트롤러 테스트")
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @Test
    @DisplayName("좌석 예약 성공")
    void createReservation_Success() throws Exception {
        // given
        ReservationRequestDto request = TestDataFactory.createReservationRequest();
        ReservationResponseDto response = ReservationResponseDto.builder()
                .employeeId(1L)
                .seatId(1L)
                .build();

        when(reservationService.createReservation(any(ReservationRequestDto.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/seats/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("예약이 완료되었습니다."))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data.employeeId").value(1))
                .andExpect(jsonPath("$.data.seatId").value(1));
    }

    @Test
    @DisplayName("좌석 예약 취소 성공")
    void cancelReservation_Success() throws Exception {
        // given
        ReservationRequestDto request = TestDataFactory.createReservationRequest();
        ReservationResponseDto response = ReservationResponseDto.builder()
                .employeeId(1L)
                .seatId(1L)
                .build();

        when(reservationService.cancelReservation(any(ReservationRequestDto.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(delete("/api/v1/seats/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("예약이 취소되었습니다."))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.employeeId").value(1))
                .andExpect(jsonPath("$.data.seatId").value(1));
    }

    @Test
    @DisplayName("예약 실패 - 유효성 검증 실패")
    void createReservation_ValidationError() throws Exception {
        // given
        ReservationRequestDto request = ReservationRequestDto.builder()
                .employeeNumber(151) // 범위 초과
                .seatNumber(1)
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/seats/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
