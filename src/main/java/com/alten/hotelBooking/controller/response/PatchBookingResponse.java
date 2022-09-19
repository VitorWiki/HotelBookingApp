package com.alten.hotelBooking.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatchBookingResponse {

    @JsonProperty("book_id")
    private Integer bookId;

    @JsonProperty("new_reservation_start_date")
    private LocalDate reservationStartDate;

    @JsonProperty("new_reservation_end_date")
    private LocalDate reservationEndDate;

    @JsonProperty("client_name")
    private String clientName;
}
