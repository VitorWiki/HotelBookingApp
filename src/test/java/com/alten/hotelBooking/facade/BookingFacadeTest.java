package com.alten.hotelBooking.facade;

import com.alten.hotelBooking.service.BookingService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BookingFacadeTest {

    @InjectMocks
    BookingFacade facade;

    @Mock
    BookingService service;

    @Before
    public void setup() {}

    @Test
    public void must_execute_booking_of_room(){

    }
}
