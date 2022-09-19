package com.alten.hotelBooking.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class GetBookingResponse {

    @JsonProperty("reservation_status")
    private String statusMessage;

    @JsonProperty("current_reservation_start_date")
    private LocalDate reservationStartDate;

    @JsonProperty("current_reservation_end_date")
    private LocalDate reservationEndDate;
}
