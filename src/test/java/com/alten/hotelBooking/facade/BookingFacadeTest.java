package com.alten.hotelBooking.facade;

import com.alten.hotelBooking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

//@RunWith
public class BookingFacadeTest {

    @InjectMocks
    BookingFacade facade;

    @Mock
    BookingService service;

    @Test
    public void must_execute_booking_of_room(){

    }
}
