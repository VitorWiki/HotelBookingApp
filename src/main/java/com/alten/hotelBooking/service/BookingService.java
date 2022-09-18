package com.alten.hotelBooking.service;

import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.PostBookingResponse;
import com.alten.hotelBooking.mapper.EntityMapper;
import com.alten.hotelBooking.repositories.RoomEntity;
import com.alten.hotelBooking.repositories.RoomRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    RoomRepository roomRepository;

    final static String BOOKING_SUCCESS_RETURN = "Your reservation is completed";

    public PostBookingResponse bookRoom(PostBookingRequest request) {

        final long daysReserved = ChronoUnit.DAYS.between(request.getReservationStartDate(), request.getReservationEndDate());

        LocalDate middleDay = null;
        LocalDate today = LocalDate.now();
        if (daysReserved == 3) {
            middleDay = request.getReservationStartDate().plusDays(1);
        }

        if(daysReserved > 3) {
            throw new RuntimeException("number of days for booking can't pass 3 days");
        }

        if(request.getReservationEndDate().isBefore(request.getReservationStartDate())) {
            throw new RuntimeException("end date must be after start date");
        }

        if(ChronoUnit.DAYS.between(request.getReservationStartDate(), LocalDate.now()) > 30) {
            throw new RuntimeException("reservations can only be made up until 30 days in the future");
        }

        if (today.isEqual(request.getReservationStartDate()) ||
                today.isEqual( request.getReservationEndDate()) ||
                today.isEqual(middleDay)) {
            throw new RuntimeException("reservations can't be made for the same day");
        }

        Optional<RoomEntity> existingRoom  = roomRepository.findByRoomContaining(request.getReservationStartDate());

        if (existingRoom.isPresent()) {
            throw new RuntimeException("Room already booked for this date");
        }

        RoomEntity newRoomReservation = roomRepository.save(Mappers.getMapper(EntityMapper.class).mapPostFrom(request, middleDay));

        return new PostBookingResponse(newRoomReservation.getBookId(), BOOKING_SUCCESS_RETURN);

    }

    public void deleteBooking(Integer bookingId) {
        try {
            roomRepository.deleteById(bookingId);
        } catch (Exception e) {
            throw new RuntimeException("No booking found to delete");
        }

    }

}
