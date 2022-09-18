package com.alten.hotelBooking.service;

import com.alten.hotelBooking.controller.request.GetBookingRequest;
import com.alten.hotelBooking.controller.request.PatchBookingRequest;
import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.GetBookingResponse;
import com.alten.hotelBooking.controller.response.PatchBookingResponse;
import com.alten.hotelBooking.controller.response.PostBookingResponse;
import com.alten.hotelBooking.mapper.EntityMapper;
import com.alten.hotelBooking.repositories.entities.RoomEntity;
import com.alten.hotelBooking.repositories.RoomRepository;
import com.alten.hotelBooking.utils.DateValidator;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    RoomRepository roomRepository;

    final static String BOOKING_SUCCESS_RETURN = "Your reservation is completed";

    public PostBookingResponse bookRoom(PostBookingRequest request) {

        LocalDate middleDay = DateValidator.getMiddleDate(request.getReservationStartDate(), request.getReservationEndDate());

        DateValidator.dateValidations(request.getReservationStartDate(), request.getReservationEndDate(), middleDay);

        Optional<RoomEntity> existingRoom  = findExistingReservation(request.getReservationStartDate());

        if (existingRoom.isPresent()) {
            throw new RuntimeException("Room already booked for this date");
        }

        RoomEntity newRoomReservation = roomRepository.save(Mappers.getMapper(EntityMapper.class).mapPostFrom(request, middleDay));

        return new PostBookingResponse(newRoomReservation.getBookId(), BOOKING_SUCCESS_RETURN);

    }

    public GetBookingResponse checkAvailability(GetBookingRequest request) {

        Optional<RoomEntity> bookedRoom = findExistingReservation(request.getReservationDay());

        if (bookedRoom.isPresent()) {
            RoomEntity foundRoom = bookedRoom.get();
            return new GetBookingResponse("Room not available", foundRoom.getReservationStartDate(), foundRoom.getReservationEndDate());
        }

        return new GetBookingResponse("Room available!", null, null);
    }

    public PatchBookingResponse updateBookingDate(PatchBookingRequest request) {

        Optional<RoomEntity> currentBook = roomRepository.findById(request.getBookId());

        if (currentBook.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No booking found");
        }

        LocalDate middleDay = DateValidator.getMiddleDate(request.getNewReservationStartDate(), request.getNewReservationEndDate());

        DateValidator.dateValidations(request.getNewReservationStartDate(), request.getNewReservationEndDate(), middleDay);

        RoomEntity updatedRoomReservation = roomRepository.save(Mappers.getMapper(EntityMapper.class).mapPatchFrom(request, currentBook.get(), middleDay));

        return Mappers.getMapper(EntityMapper.class).mapFrom(updatedRoomReservation);

    }

    public void deleteBooking(Integer bookingId) {
        try {
            roomRepository.deleteById(bookingId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No booking found to delete");
        }

    }

    private Optional<RoomEntity> findExistingReservation(LocalDate bookStartDate) {
        return roomRepository.findByRoomContaining(bookStartDate);
    }

}
