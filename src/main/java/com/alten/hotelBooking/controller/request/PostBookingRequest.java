package com.alten.hotelBooking.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PostBookingRequest {

    @NotNull
    @JsonProperty("client_name")
    private String clientName;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("reservation_start_date")
    private LocalDate reservationStartDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("reservation_end_date")
    private LocalDate reservationEndDate;

}
