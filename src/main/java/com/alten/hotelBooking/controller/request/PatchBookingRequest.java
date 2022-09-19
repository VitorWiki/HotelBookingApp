package com.alten.hotelBooking.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PatchBookingRequest {

    @NotNull
    @JsonProperty("book_id")
    private Integer bookId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("new_reservation_start_date")
    private LocalDate newReservationStartDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("new_reservation_end_date")
    private LocalDate newReservationEndDate;
}
