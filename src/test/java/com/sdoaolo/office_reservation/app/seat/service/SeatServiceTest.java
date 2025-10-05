package com.sdoaolo.office_reservation.app.seat.service;

import com.sdoaolo.office_reservation.app.common.TestDataFactory;
import com.sdoaolo.office_reservation.app.seat.dto.SeatCreateRequestDto;
import com.sdoaolo.office_reservation.app.seat.dto.SeatResponseDto;
import com.sdoaolo.office_reservation.app.seat.entity.Seat;
import com.sdoaolo.office_reservation.app.seat.repository.SeatRepository;
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
@DisplayName("좌석 서비스 테스트")
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @Test
    @DisplayName("좌석 등록 성공")
    void createSeat_Success() {
        // given
        SeatCreateRequestDto request = TestDataFactory.createSeatRequest();
        Seat savedSeat = TestDataFactory.createSeat();
        savedSeat.setId(1L);

        when(seatRepository.existsBySeatNumber(1)).thenReturn(false);
        when(seatRepository.save(any(Seat.class))).thenReturn(savedSeat);

        // when
        SeatResponseDto result = seatService.createSeat(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getSeatNumber()).isEqualTo(1);
        assertThat(result.getSeatLocation()).isEqualTo("롯데월드타워 27F Room A");
    }

    @Test
    @DisplayName("좌석 등록 실패 - 중복 좌석 번호")
    void createSeat_DuplicateSeatNumber() {
        // given
        SeatCreateRequestDto request = TestDataFactory.createSeatRequest();
        when(seatRepository.existsBySeatNumber(1)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> seatService.createSeat(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 좌석 번호입니다.");
    }

    @Test
    @DisplayName("좌석 목록 조회 성공")
    void getAllSeats_Success() {
        // given
        Seat seat = TestDataFactory.createSeat();
        seat.setId(1L);
        Page<Seat> page = new PageImpl<>(List.of(seat), PageRequest.of(0, 20), 1);

        when(seatRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        // when
        Page<SeatResponseDto> result = seatService.getAllSeats(PageRequest.of(0, 20));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getSeatNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("좌석 상세 조회 성공")
    void getSeatByNumber_Success() {
        // given
        Seat seat = TestDataFactory.createSeat();
        seat.setId(1L);

        when(seatRepository.findBySeatNumber(1)).thenReturn(Optional.of(seat));

        // when
        SeatResponseDto result = seatService.getSeatByNumber(1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getSeatNumber()).isEqualTo(1);
        assertThat(result.getSeatLocation()).isEqualTo("롯데월드타워 27F Room A");
    }

    @Test
    @DisplayName("좌석 상세 조회 실패 - 좌석 없음")
    void getSeatByNumber_SeatNotFound() {
        // given
        when(seatRepository.findBySeatNumber(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> seatService.getSeatByNumber(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Seat Not Found");
    }
}
