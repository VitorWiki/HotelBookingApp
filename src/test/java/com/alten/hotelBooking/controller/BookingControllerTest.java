package com.alten.hotelBooking.controller;

import com.alten.hotelBooking.controller.request.PatchBookingRequest;
import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.GetBookingResponse;
import com.alten.hotelBooking.controller.response.PatchBookingResponse;
import com.alten.hotelBooking.controller.response.PostBookingResponse;
import com.alten.hotelBooking.facade.BookingFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class BookingControllerTest {

    @Mock
    BookingFacade facade;

    @InjectMocks
    BookingController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void must_call_post_method() {
        PostBookingRequest postRequest = new PostBookingRequest("Vitor", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        PostBookingResponse postResponse = new PostBookingResponse(1, "Your reservation is completed");

        Mockito.when(facade.bookingRoom(postRequest)).thenReturn(postResponse);

        ResponseEntity<PostBookingResponse> postReturn = controller.postBookRoom(postRequest);

        Assertions.assertNotNull(postReturn);
        Mockito.verify(facade, Mockito.times(1)).bookingRoom(postRequest);
    }

    @Test
    public void must_call_get_method() {
        GetBookingResponse getResponse = new GetBookingResponse("Room available for booking on this day",
                LocalDate.now(), LocalDate.now());

        Mockito.when(facade.checkingAvailableDate(LocalDate.now())).thenReturn(getResponse);

        ResponseEntity<GetBookingResponse> postReturn = controller.getBookedRoom(LocalDate.now());

        Assertions.assertNotNull(postReturn);
        Mockito.verify(facade, Mockito.times(1)).checkingAvailableDate(LocalDate.now());
    }

    @Test
    public void must_call_patch_method() {
        PatchBookingRequest patchRequest = new PatchBookingRequest(1, LocalDate.now(), LocalDate.now().plusDays(1));
        PatchBookingResponse patchResponse = new PatchBookingResponse();

        Mockito.when(facade.updatingBook(patchRequest)).thenReturn(patchResponse);

        ResponseEntity<PatchBookingResponse> postReturn = controller.patchBookedRoom(patchRequest);

        Assertions.assertNotNull(postReturn);
        Mockito.verify(facade, Mockito.times(1)).updatingBook(patchRequest);
    }

    @Test
    public void must_call_delete_method() {

        Mockito.doNothing().when(facade).deletingBook(1);

        controller.deleteBooking(1);

        Mockito.verify(facade, Mockito.times(1)).deletingBook(1);
    }
}
