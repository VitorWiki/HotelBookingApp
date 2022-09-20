package com.alten.hotelBooking.facade;

import com.alten.hotelBooking.controller.request.PatchBookingRequest;
import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.GetBookingResponse;
import com.alten.hotelBooking.controller.response.PatchBookingResponse;
import com.alten.hotelBooking.controller.response.PostBookingResponse;
import com.alten.hotelBooking.service.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class BookingFacadeTest {

    @InjectMocks
    BookingFacade facade;

    @Mock
    BookingService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void must_execute_booking_of_room(){
        PostBookingRequest postRequest = new PostBookingRequest("Vitor", LocalDate.now(), LocalDate.now().plusDays(1));
        PostBookingResponse postResponse = new PostBookingResponse(1, "Your reservation is completed");

        Mockito.when(service.bookRoom(postRequest)).thenReturn(postResponse);

        PostBookingResponse facadeReturn = facade.bookingRoom(postRequest);

        Assertions.assertNotNull(facadeReturn);
        Mockito.verify(service, Mockito.times(1)).bookRoom(postRequest);

    }

    @Test
    public void must_execute_search_of_available_room(){

        GetBookingResponse getResponse = new GetBookingResponse("Room available for booking on this day",
                LocalDate.now(), LocalDate.now());

        LocalDate testDate = LocalDate.now();

        Mockito.when(service.checkAvailability(testDate)).thenReturn(getResponse);

        GetBookingResponse facadeReturn = facade.checkingAvailableDate(LocalDate.now());

        Assertions.assertNotNull(facadeReturn);
        Mockito.verify(service, Mockito.times(1)).checkAvailability(testDate);

    }

    @Test
    public void must_execute_update_of_book(){
        PatchBookingRequest patchRequest = new PatchBookingRequest(1, LocalDate.now(), LocalDate.now().plusDays(1));
        PatchBookingResponse patchResponse = new PatchBookingResponse();

        Mockito.when(service.updateBookingDate(patchRequest)).thenReturn(patchResponse);

        PatchBookingResponse facadeReturn = facade.updatingBook(patchRequest);

        Assertions.assertNotNull(facadeReturn);
        Mockito.verify(service, Mockito.times(1)).updateBookingDate(patchRequest);

    }

    @Test
    public void must_execute_book_delete(){

        Integer bookId = 1;

        Mockito.doNothing().when(service).deleteBooking(bookId);

        facade.deletingBook(bookId);

        Mockito.verify(service, Mockito.times(1)).deleteBooking(bookId);

    }
}
