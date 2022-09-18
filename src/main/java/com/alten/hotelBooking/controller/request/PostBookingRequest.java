package com.alten.hotelBooking.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PostBookingRequest {

    @JsonProperty("reservation_start_date")
    @NonNull
    private LocalDate reservationStartDate;

    @JsonProperty("reservation_end_date")
    @NonNull
    private LocalDate reservationEndDate;

    @JsonProperty("client_name")
    @NonNull
    private String clientName;
}
