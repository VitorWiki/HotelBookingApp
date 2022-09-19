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

    @JsonProperty("reservation_start_day")
    private LocalDate reservationStartDate;

    @JsonProperty("reservation_end_day")
    private LocalDate reservationEndDate;
}
