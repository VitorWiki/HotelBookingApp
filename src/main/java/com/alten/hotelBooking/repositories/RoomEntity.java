package com.alten.hotelBooking.repositories;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "room")
public class RoomEntity {

    @Id
    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "reservation_start_date")
    private LocalDate reservationStartDate;

    @Column(name = "reservation_middle_date")
    private LocalDate reservationMiddleDate;

    @Column(name = "reservation_end_date")
    private LocalDate reservationEndDate;

}
