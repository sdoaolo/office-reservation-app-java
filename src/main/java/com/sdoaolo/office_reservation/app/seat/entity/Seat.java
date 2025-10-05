package com.sdoaolo.office_reservation.app.seat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    private Long id;

    @Column(name = "seat_number", nullable = false, unique = true)
    private Integer seatNumber; // 좌석 고유 번호 (1~100)

    @Column(name = "seat_location", length = 50)
    private String seatLocation; // 좌석 위치

    @Column(name = "created_date")
    private LocalDate createdDate; // 좌석 생성일
}