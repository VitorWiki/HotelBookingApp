package com.alten.hotelBooking.controller;

import com.alten.hotelBooking.controller.request.PatchBookingRequest;
import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.GetBookingResponse;
import com.alten.hotelBooking.controller.response.PatchBookingResponse;
import com.alten.hotelBooking.controller.response.PostBookingResponse;
import com.alten.hotelBooking.facade.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/v1/booking_app")
public class BookingController {

    @Autowired
    BookingFacade facade;

    @PostMapping(value = "/book_room")
    public ResponseEntity<PostBookingResponse> postBookRoom(@Valid @RequestBody PostBookingRequest request) {

        return ResponseEntity.ok().body(facade.bookingRoom(request));
    }

    @GetMapping(value = "/check_room_availability/{reservation_day}")
    public ResponseEntity<GetBookingResponse> getBookedRoom(@PathVariable("reservation_day")
                                                                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reservationDay) {

        return ResponseEntity.ok().body(facade.checkingRoom(reservationDay));
    }

    @PatchMapping(value = "/update_booking")
    public ResponseEntity<PatchBookingResponse> patchBookedRoom(@Valid @RequestBody PatchBookingRequest request) {

        return ResponseEntity.ok().body(facade.updatingBook(request));
    }

    @DeleteMapping(value = "/delete_booking/{book_id}")
    public ResponseEntity<HttpStatus> deleteBooking(@PathVariable("book_id") Integer bookId) {

        facade.deletingBook(bookId);

        return ResponseEntity.noContent().build();
    }
}
