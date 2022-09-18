package com.alten.hotelBooking.controller;

import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.PostBookingResponse;
import com.alten.hotelBooking.facade.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/booking_app")
public class BookingController {

    @Autowired
    BookingFacade facade;

    @PostMapping(value = "/book")
    public ResponseEntity<PostBookingResponse> postBookRoom(@RequestBody PostBookingRequest request) {

        return ResponseEntity.ok().body(facade.bookingRoom(request));
    }

}
