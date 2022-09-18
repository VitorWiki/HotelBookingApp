package com.alten.hotelBooking.facade;

import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.PostBookingResponse;
import com.alten.hotelBooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingFacade {

    @Autowired
    BookingService bookingService;



    public PostBookingResponse bookingRoom(PostBookingRequest request) {

        return bookingService.bookRoom(request);
    }
}
