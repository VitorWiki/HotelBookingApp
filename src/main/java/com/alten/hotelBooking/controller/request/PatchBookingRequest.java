package com.alten.hotelBooking.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PatchBookingRequest {

    @JsonProperty("book_id")
    @NonNull
    private Integer bookId;

    @JsonProperty("new_reservation_start_date")
    @NonNull
    private LocalDate newReservationStartDate;

    @JsonProperty("new_reservation_end_date")
    @NonNull
    private LocalDate newReservationEndDate;
}
