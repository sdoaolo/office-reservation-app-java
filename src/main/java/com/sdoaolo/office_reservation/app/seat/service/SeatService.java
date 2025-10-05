package com.sdoaolo.office_reservation.app.seat.service;

import com.sdoaolo.office_reservation.app.seat.dto.SeatCreateRequestDto;
import com.sdoaolo.office_reservation.app.seat.dto.SeatResponseDto;
import com.sdoaolo.office_reservation.app.seat.entity.Seat;
import com.sdoaolo.office_reservation.app.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatService {

    private final SeatRepository seatRepository;

    @Transactional
    public SeatResponseDto createSeat(SeatCreateRequestDto request) {
        // 좌석 번호 중복 확인
        if (seatRepository.existsBySeatNumber(request.getSeatNumber())) {
            throw new IllegalArgumentException("이미 존재하는 좌석 번호입니다.");
        }

        Seat seat = Seat.builder()
                .seatNumber(request.getSeatNumber())
                .seatLocation(request.getSeatLocation())
                .createdDate(request.getCreatedDate() != null ? request.getCreatedDate() : java.time.LocalDate.now())
                .build();

        Seat savedSeat = seatRepository.save(seat);

        return SeatResponseDto.builder()
                .id(savedSeat.getId())
                .seatNumber(savedSeat.getSeatNumber())
                .seatLocation(savedSeat.getSeatLocation())
                .createdDate(savedSeat.getCreatedDate())
                .build();
    }

    public Page<SeatResponseDto> getAllSeats(Pageable pageable) {
        return seatRepository.findAll(pageable)
                .map(seat -> SeatResponseDto.builder()
                        .id(seat.getId())
                        .seatNumber(seat.getSeatNumber())
                        .seatLocation(seat.getSeatLocation())
                        .createdDate(seat.getCreatedDate())
                        .build());
    }

    public SeatResponseDto getSeatByNumber(Integer seatNumber) {
        Seat seat = seatRepository.findBySeatNumber(seatNumber)
                .orElseThrow(() -> new IllegalArgumentException("Seat Not Found"));
        
        return SeatResponseDto.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .seatLocation(seat.getSeatLocation())
                .createdDate(seat.getCreatedDate())
                .build();
    }
}
