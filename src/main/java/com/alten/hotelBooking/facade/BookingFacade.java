package com.alten.hotelBooking.facade;

import com.alten.hotelBooking.controller.request.PatchBookingRequest;
import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.GetBookingResponse;
import com.alten.hotelBooking.controller.response.PatchBookingResponse;
import com.alten.hotelBooking.controller.response.PostBookingResponse;
import com.alten.hotelBooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookingFacade {

    @Autowired
    BookingService bookingService;

    public PostBookingResponse bookingRoom(PostBookingRequest request) {

        return bookingService.bookRoom(request);
    }

    public GetBookingResponse checkingRoom(LocalDate reservationDay) {

        return bookingService.checkAvailability(reservationDay);
    }

    public PatchBookingResponse updatingBook(PatchBookingRequest request) {

        return bookingService.updateBookingDate(request);
    }

    public void deletingBook(Integer bookId) {

        bookingService.deleteBooking(bookId);
    }

}
