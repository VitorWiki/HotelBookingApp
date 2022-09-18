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
public class GetBookingRequest {

    @JsonProperty("reservation_day")
    @NonNull
    private LocalDate reservationDay;

}
