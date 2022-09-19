package com.alten.hotelBooking.service;

import com.alten.hotelBooking.controller.request.PatchBookingRequest;
import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.GetBookingResponse;
import com.alten.hotelBooking.controller.response.PatchBookingResponse;
import com.alten.hotelBooking.controller.response.PostBookingResponse;
import com.alten.hotelBooking.enums.ErrorEnum;
import com.alten.hotelBooking.mapper.EntityMapper;
import com.alten.hotelBooking.repositories.entities.RoomEntity;
import com.alten.hotelBooking.repositories.RoomRepository;
import com.alten.hotelBooking.utils.DateUtil;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookingService {

    @Autowired
    RoomRepository roomRepository;

    final static String BOOKING_SUCCESS_RETURN = "Your reservation is completed";
    final static String ROOM_AVAILABLE_FOR_BOOKING = "Room available for booking on this day";

    public PostBookingResponse bookRoom(PostBookingRequest request) {

        DateUtil.dateValidations(request.getReservationStartDate(), request.getReservationEndDate());

        LocalDate middleDay = DateUtil.getMiddleDate(request.getReservationStartDate(), request.getReservationEndDate());

        Optional<RoomEntity> existingRoom  = findExistingReservation(request.getReservationStartDate());

        if (existingRoom.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorEnum.ROOM_ALREADY_BOOKED.getDescription());
        }

        RoomEntity newRoomReservation = roomRepository.save(Mappers.getMapper(EntityMapper.class).mapPostFrom(request, middleDay));

        return new PostBookingResponse(newRoomReservation.getBookId(), BOOKING_SUCCESS_RETURN);

    }

    public GetBookingResponse checkAvailability(LocalDate reservationDay) {

        Optional<RoomEntity> bookedRoom = findExistingReservation(reservationDay);

        if (bookedRoom.isPresent()) {
            RoomEntity foundRoom = bookedRoom.get();
            return Mappers.getMapper(EntityMapper.class).mapGetFromExistingEntity(foundRoom);
        }
        return new GetBookingResponse(ROOM_AVAILABLE_FOR_BOOKING, null, null);
    }

    public PatchBookingResponse updateBookingDate(PatchBookingRequest request) {

        Optional<RoomEntity> currentBook = roomRepository.findById(request.getBookId());

        if (currentBook.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorEnum.NO_RESERVATION_FOUND.getDescription());
        }

        DateUtil.dateValidations(request.getNewReservationStartDate(), request.getNewReservationEndDate());

        LocalDate middleDay = DateUtil.getMiddleDate(request.getNewReservationStartDate(), request.getNewReservationEndDate());

        if (!isUpdatePossibleOnSelectedDate(request, middleDay)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorEnum.ROOM_ALREADY_BOOKED.getDescription());
        }

        RoomEntity updatedRoomReservation = roomRepository.save(Mappers.getMapper(EntityMapper.class).mapPatchFrom(request, currentBook.get(), middleDay));

        return Mappers.getMapper(EntityMapper.class).mapPatchFromEntity(updatedRoomReservation);

    }

    public void deleteBooking(Integer bookingId) {
        try {
            roomRepository.deleteById(bookingId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorEnum.NO_RESERVATION_FOUND.getDescription());
        }

    }

    private Optional<RoomEntity> findExistingReservation(LocalDate bookStartDate) {

        Optional<RoomEntity> firstDayBook = roomRepository.findByReservationStartDate(bookStartDate);
        Optional<RoomEntity> midDayBook = roomRepository.findByReservationMiddleDate(bookStartDate);
        Optional<RoomEntity> lastDayBook = roomRepository.findByReservationEndDate(bookStartDate);

        List<Optional<RoomEntity>> existingBookedRoomList = Arrays.asList(firstDayBook, midDayBook, lastDayBook);

        return existingBookedRoomList.stream().filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }

    private boolean isUpdatePossibleOnSelectedDate(PatchBookingRequest request, LocalDate middleDate) {

        Optional<RoomEntity> firstDayBook = findExistingReservation(request.getNewReservationStartDate());
        Optional<RoomEntity> lastDayBook = findExistingReservation(request.getNewReservationEndDate());
        Optional<RoomEntity> middleDayBook = null == middleDate ? Optional.empty() : findExistingReservation(middleDate);

        List<Optional<RoomEntity>> existingBookedRoomList = Arrays.asList(firstDayBook, middleDayBook, lastDayBook);

        List<RoomEntity> sameTimeReservations = new ArrayList<>();

       existingBookedRoomList.stream().filter(Optional::isPresent).forEach(RoomEntity -> {
            RoomEntity room = RoomEntity.get();
            if (!room.getBookId().equals(request.getBookId())) {
                sameTimeReservations.add(room);
            }
        });

        return existingBookedRoomList.stream().allMatch(Optional::isEmpty) || sameTimeReservations.isEmpty();
    }

}
