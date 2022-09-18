package com.alten.hotelBooking.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;

import java.time.LocalDate;


public class PatchBookingResponse {

    @JsonProperty("book_id")
    @NonNull
    private Integer bookId;

    @JsonProperty("new_reservation_start_date")
    @NonNull
    private LocalDate reservationStartDate;

    @JsonProperty("new_reservation_end_date")
    @NonNull
    private LocalDate reservationEndDate;

    @JsonProperty("client_name")
    @NonNull
    private String clientName;
}
