package com.alten.hotelBooking.service;

import com.alten.hotelBooking.controller.request.PatchBookingRequest;
import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.GetBookingResponse;
import com.alten.hotelBooking.controller.response.PatchBookingResponse;
import com.alten.hotelBooking.controller.response.PostBookingResponse;
import com.alten.hotelBooking.repositories.RoomRepository;
import com.alten.hotelBooking.repositories.entities.RoomEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    @Mock
    RoomRepository roomRepository;

    @InjectMocks
    BookingService service;

    RoomEntity previouslyBookedRoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        previouslyBookedRoom = new RoomEntity(1, "Vitor", LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2), LocalDate.now().plusDays(3));
    }

    @Test
    public void must_execute_new_booking_with_success() {

        PostBookingRequest postRequest = new PostBookingRequest("Vitor", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        RoomEntity newBookedRoom = previouslyBookedRoom;

        Mockito.when(roomRepository.findByReservationStartDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationMiddleDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationEndDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.save(Mockito.any(RoomEntity.class))).thenReturn(newBookedRoom);

        PostBookingResponse serviceReturn = service.bookRoom(postRequest);

        Assertions.assertNotNull(serviceReturn);
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationStartDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationMiddleDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationEndDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(1)).save(Mockito.any(RoomEntity.class));
    }

    @Test
    public void must_execute_new_booking_and_fail_due_to_existing_book_on_existing_start_date() {

        PostBookingRequest postRequest = new PostBookingRequest("Vitor", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        Mockito.when(roomRepository.findByReservationStartDate(Mockito.any(LocalDate.class))).thenReturn(Optional.of(previouslyBookedRoom));
        Mockito.when(roomRepository.findByReservationMiddleDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationEndDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(ResponseStatusException.class, () -> service.bookRoom(postRequest));

        Assertions.assertEquals(exception.getMessage(), HttpStatus.CONFLICT + " \"Room already booked for this date\"");
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationStartDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationMiddleDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationEndDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(0)).save(Mockito.any(RoomEntity.class));
    }

    @Test
    public void must_execute_new_booking_and_fail_due_to_existing_book_on_existing_middle_date() {

        PostBookingRequest postRequest = new PostBookingRequest("Vitor", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        Mockito.when(roomRepository.findByReservationStartDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationMiddleDate(Mockito.any(LocalDate.class))).thenReturn(Optional.of(previouslyBookedRoom));
        Mockito.when(roomRepository.findByReservationEndDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(ResponseStatusException.class, () -> service.bookRoom(postRequest));

        Assertions.assertEquals(exception.getMessage(), HttpStatus.CONFLICT + " \"Room already booked for this date\"");
    }

    @Test
    public void must_execute_new_booking_and_fail_due_to_existing_book_on_existing_final_date() {

        PostBookingRequest postRequest = new PostBookingRequest("Vitor", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        Mockito.when(roomRepository.findByReservationStartDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationMiddleDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationEndDate(Mockito.any(LocalDate.class))).thenReturn(Optional.of(previouslyBookedRoom));

        Throwable exception = Assertions.assertThrows(ResponseStatusException.class, () -> service.bookRoom(postRequest));

        Assertions.assertEquals(exception.getMessage(), HttpStatus.CONFLICT + " \"Room already booked for this date\"");
    }

    @Test
    public void must_search_available_booking_date_but_find_previously_made_reservation() {

        Mockito.when(roomRepository.findByReservationStartDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationMiddleDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationEndDate(Mockito.any(LocalDate.class))).thenReturn(Optional.of(previouslyBookedRoom));

        GetBookingResponse getExpectedReturn = service.checkAvailability(LocalDate.now());

        Assertions.assertNotNull(getExpectedReturn);
        Assertions.assertEquals(getExpectedReturn.getStatusMessage(),"Room not available for booking on this day");
        Assertions.assertEquals(getExpectedReturn.getReservationStartDate(), previouslyBookedRoom.getReservationStartDate());
        Assertions.assertEquals(getExpectedReturn.getReservationEndDate(), previouslyBookedRoom.getReservationEndDate());
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationStartDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationMiddleDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationEndDate(Mockito.any(LocalDate.class));
    }

    @Test
    public void must_search_available_booking_date_successfully() {

        Mockito.when(roomRepository.findByReservationStartDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationMiddleDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationEndDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());

        GetBookingResponse getExpectedReturn = service.checkAvailability(LocalDate.now());

        Assertions.assertNotNull(getExpectedReturn);
        Assertions.assertEquals(getExpectedReturn.getStatusMessage(),"Room available for booking on this day");
        Assertions.assertNull(getExpectedReturn.getReservationStartDate());
        Assertions.assertNull(getExpectedReturn.getReservationEndDate());
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationStartDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationMiddleDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(1)).findByReservationEndDate(Mockito.any(LocalDate.class));
    }

    @Test
    public void must_delete_reservation() {

        Mockito.doNothing().when(roomRepository).deleteById(1);

        service.deleteBooking(1);

        Mockito.verify(roomRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    public void must_fail_to_delete_non_existing_reservation() {

        Mockito.doThrow(ResponseStatusException.class).when(roomRepository).deleteById(1);

        Throwable exception = Assertions.assertThrows(ResponseStatusException.class, () -> service.deleteBooking(1));

        Assertions.assertEquals(exception.getMessage(), HttpStatus.NOT_FOUND + " \"No reservation found with this id\"");
        Mockito.verify(roomRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    public void must_update_reservation_dates_with_no_matching_reservations_and_middle_day() {

        PatchBookingRequest request = new PatchBookingRequest(1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        RoomEntity updatedRoom = previouslyBookedRoom;

        Mockito.when(roomRepository.findByReservationStartDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationMiddleDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationEndDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.save(Mockito.any(RoomEntity.class))).thenReturn(updatedRoom);
        Mockito.when(roomRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(previouslyBookedRoom));

        PatchBookingResponse patchExpectedReturn = service.updateBookingDate(request);

        Assertions.assertNotNull(patchExpectedReturn);
        Mockito.verify(roomRepository, Mockito.times(1)).findById(Mockito.anyInt());
        Mockito.verify(roomRepository, Mockito.times(1)).save(Mockito.any(RoomEntity.class));
        Mockito.verify(roomRepository, Mockito.times(3)).findByReservationStartDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(3)).findByReservationStartDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(3)).findByReservationMiddleDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(3)).findByReservationEndDate(Mockito.any(LocalDate.class));
    }

    @Test
    public void must_update_reservation_dates_with_no_matching_reservations_without_middle_day() {

        PatchBookingRequest request = new PatchBookingRequest(1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        RoomEntity updatedRoom = previouslyBookedRoom;

        Mockito.when(roomRepository.findByReservationStartDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationMiddleDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationEndDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.save(Mockito.any(RoomEntity.class))).thenReturn(updatedRoom);
        Mockito.when(roomRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(previouslyBookedRoom));

        PatchBookingResponse patchExpectedReturn = service.updateBookingDate(request);

        Assertions.assertNotNull(patchExpectedReturn);
        Mockito.verify(roomRepository, Mockito.times(1)).findById(Mockito.anyInt());
        Mockito.verify(roomRepository, Mockito.times(1)).save(Mockito.any(RoomEntity.class));
        Mockito.verify(roomRepository, Mockito.times(2)).findByReservationStartDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(2)).findByReservationMiddleDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(2)).findByReservationEndDate(Mockito.any(LocalDate.class));
    }

    @Test
    public void must_update_reservation_dates_with_matching_reservations_but_owned_by_the_user() {

        PatchBookingRequest request = new PatchBookingRequest(1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        RoomEntity matchingRoom1 = new RoomEntity(1, "Vitor", LocalDate.now().plusDays(1),
                null, LocalDate.now().plusDays(2));
        RoomEntity updatedRoom = previouslyBookedRoom;

        Mockito.when(roomRepository.findByReservationStartDate(Mockito.any(LocalDate.class))).thenReturn(Optional.of(matchingRoom1));
        Mockito.when(roomRepository.findByReservationMiddleDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationEndDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.save(Mockito.any(RoomEntity.class))).thenReturn(updatedRoom);
        Mockito.when(roomRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(previouslyBookedRoom));

        PatchBookingResponse patchExpectedReturn = service.updateBookingDate(request);

        Assertions.assertNotNull(patchExpectedReturn);
        Mockito.verify(roomRepository, Mockito.times(1)).findById(Mockito.anyInt());
        Mockito.verify(roomRepository, Mockito.times(1)).save(Mockito.any(RoomEntity.class));
        Mockito.verify(roomRepository, Mockito.times(2)).findByReservationStartDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(2)).findByReservationMiddleDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(2)).findByReservationEndDate(Mockito.any(LocalDate.class));
    }

    @Test
    public void must_fail_to_update_reservation_because_of_unexisting_id() {

        PatchBookingRequest request = new PatchBookingRequest(1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        Mockito.when(roomRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(ResponseStatusException.class, () -> service.updateBookingDate(request));

        Assertions.assertEquals(exception.getMessage(), HttpStatus.NOT_FOUND + " \"No reservation found with this id\"");
        Mockito.verify(roomRepository, Mockito.times(1)).findById(Mockito.anyInt());
        Mockito.verify(roomRepository, Mockito.times(0)).save(Mockito.any(RoomEntity.class));
        Mockito.verify(roomRepository, Mockito.times(0)).findByReservationStartDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(0)).findByReservationMiddleDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(0)).findByReservationEndDate(Mockito.any(LocalDate.class));
    }

    @Test
    public void must_fail_to_update_reservation_dates_with_matching_reservations() {

        PatchBookingRequest request = new PatchBookingRequest(1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        RoomEntity matchingRoom1 = new RoomEntity(2, "Vitor", LocalDate.now().plusDays(1),
                null, LocalDate.now().plusDays(2));

        Mockito.when(roomRepository.findByReservationStartDate(Mockito.any(LocalDate.class))).thenReturn(Optional.of(matchingRoom1));
        Mockito.when(roomRepository.findByReservationMiddleDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findByReservationEndDate(Mockito.any(LocalDate.class))).thenReturn(Optional.empty());
        Mockito.when(roomRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(previouslyBookedRoom));

        Throwable exception = Assertions.assertThrows(ResponseStatusException.class, () -> service.updateBookingDate(request));

        Assertions.assertEquals(exception.getMessage(), HttpStatus.CONFLICT + " \"Room already booked for this date\"");
        Mockito.verify(roomRepository, Mockito.times(1)).findById(Mockito.anyInt());
        Mockito.verify(roomRepository, Mockito.times(0)).save(Mockito.any(RoomEntity.class));
        Mockito.verify(roomRepository, Mockito.times(2)).findByReservationStartDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(2)).findByReservationMiddleDate(Mockito.any(LocalDate.class));
        Mockito.verify(roomRepository, Mockito.times(2)).findByReservationEndDate(Mockito.any(LocalDate.class));
    }
}
