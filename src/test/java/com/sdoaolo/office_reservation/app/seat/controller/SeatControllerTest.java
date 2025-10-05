package com.sdoaolo.office_reservation.app.seat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdoaolo.office_reservation.app.common.TestDataFactory;
import com.sdoaolo.office_reservation.app.seat.dto.SeatCreateRequestDto;
import com.sdoaolo.office_reservation.app.seat.dto.SeatResponseDto;
import com.sdoaolo.office_reservation.app.seat.service.SeatService;
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

@WebMvcTest(SeatController.class)
@DisplayName("좌석 컨트롤러 테스트")
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SeatService seatService;

    @Test
    @DisplayName("좌석 등록 성공")
    void createSeat_Success() throws Exception {
        // given
        SeatCreateRequestDto request = TestDataFactory.createSeatRequest();
        SeatResponseDto response = SeatResponseDto.builder()
                .id(1L)
                .seatNumber(1)
                .seatLocation("롯데월드타워 27F Room A")
                .createdDate(LocalDate.now())
                .build();

        when(seatService.createSeat(any(SeatCreateRequestDto.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("좌석이 등록되었습니다."))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.seatNumber").value(1))
                .andExpect(jsonPath("$.data.seatLocation").value("롯데월드타워 27F Room A"));
    }

    @Test
    @DisplayName("좌석 목록 조회 성공")
    void getAllSeats_Success() throws Exception {
        // given
        SeatResponseDto seat = SeatResponseDto.builder()
                .id(1L)
                .seatNumber(1)
                .seatLocation("롯데월드타워 27F Room A")
                .createdDate(LocalDate.now())
                .build();

        Page<SeatResponseDto> page = new PageImpl<>(List.of(seat), PageRequest.of(0, 20), 1);
        when(seatService.getAllSeats(any()))
                .thenReturn(page);

        // when & then
        mockMvc.perform(get("/api/v1/seats")
                        .param("page", "1")
                        .param("size", "20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("좌석 목록을 조회했습니다."))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].seatNumber").value(1));
    }

    @Test
    @DisplayName("좌석 상세 조회 성공")
    void getSeatByNumber_Success() throws Exception {
        // given
        SeatResponseDto response = SeatResponseDto.builder()
                .id(1L)
                .seatNumber(1)
                .seatLocation("롯데월드타워 27F Room A")
                .createdDate(LocalDate.now())
                .build();

        when(seatService.getSeatByNumber(anyInt()))
                .thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/seats/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("좌석 정보를 조회했습니다."))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.seatNumber").value(1))
                .andExpect(jsonPath("$.data.seatLocation").value("롯데월드타워 27F Room A"));
    }

    @Test
    @DisplayName("좌석 등록 실패 - 유효성 검증 실패")
    void createSeat_ValidationError() throws Exception {
        // given
        SeatCreateRequestDto request = SeatCreateRequestDto.builder()
                .seatNumber(101) // 범위 초과
                .seatLocation("롯데월드타워 27F Room A")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
