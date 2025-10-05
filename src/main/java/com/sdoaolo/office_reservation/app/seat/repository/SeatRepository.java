package com.sdoaolo.office_reservation.app.seat.repository;

import com.sdoaolo.office_reservation.app.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findBySeatNumber(Integer seatNumber);
    boolean existsBySeatNumber(Integer seatNumber);
}
