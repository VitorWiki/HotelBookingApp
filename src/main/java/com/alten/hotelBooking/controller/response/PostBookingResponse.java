package com.alten.hotelBooking.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBookingResponse {

    @JsonProperty("book_id")
    private Integer bookId;

    @JsonProperty("booking_return_message")
    private String bookingReturnMessage;
}
