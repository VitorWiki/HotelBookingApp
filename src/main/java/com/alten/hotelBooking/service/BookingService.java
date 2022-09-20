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
import com.alten.hotelBooking.utils.ReservationDateUtil;
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

        //Validates if the date values are ok before continuing
        ReservationDateUtil.dateValidations(request.getReservationStartDate(), request.getReservationEndDate());
        //finds middle date if user makes a 3 day reservation
        LocalDate middleDay = ReservationDateUtil.getMiddleDate(request.getReservationStartDate(), request.getReservationEndDate());
        //if reservation for requested date already exists, returns a error to the user
        if (findExistingReservation(request.getReservationStartDate()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorEnum.ROOM_ALREADY_BOOKED.getDescription());
        }

        RoomEntity newRoomReservation = roomRepository.save(Mappers.getMapper(EntityMapper.class).mapPostFrom(request, middleDay));

        return new PostBookingResponse(newRoomReservation.getBookId(), BOOKING_SUCCESS_RETURN);

    }

    public GetBookingResponse checkAvailability(LocalDate reservationDay) {
        //search for reservations made on the requested date
        Optional<RoomEntity> bookedRoom = findExistingReservation(reservationDay);

        if (bookedRoom.isPresent()) {
            RoomEntity foundRoom = bookedRoom.get();
            //if a reservation exists, returns to the user how long it goes
            return Mappers.getMapper(EntityMapper.class).mapGetFromExistingEntity(foundRoom);
        }
        //if a reservation does not exist, returns to the user that the room is available
        return new GetBookingResponse(ROOM_AVAILABLE_FOR_BOOKING, null, null);
    }

    public PatchBookingResponse updateBookingDate(PatchBookingRequest request) {

        Optional<RoomEntity> currentBook = roomRepository.findById(request.getBookId());

        //if the reservation to be updated does not exist, returns an error to the user
        if (currentBook.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorEnum.NO_RESERVATION_FOUND.getDescription());
        }

        ReservationDateUtil.dateValidations(request.getNewReservationStartDate(), request.getNewReservationEndDate());

        LocalDate middleDay = ReservationDateUtil.getMiddleDate(request.getNewReservationStartDate(), request.getNewReservationEndDate());

        //checks if a reservation already exists at the date the user wants to update, and if it does and is not from the requester, throws an error
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

    //search for possible existing reservations at the same date requested
    private Optional<RoomEntity> findExistingReservation(LocalDate bookStartDate) {

        Optional<RoomEntity> firstDayBook = roomRepository.findByReservationStartDate(bookStartDate);
        Optional<RoomEntity> midDayBook = roomRepository.findByReservationMiddleDate(bookStartDate);
        Optional<RoomEntity> lastDayBook = roomRepository.findByReservationEndDate(bookStartDate);

        List<Optional<RoomEntity>> existingBookedRoomList = Arrays.asList(firstDayBook, midDayBook, lastDayBook);

        return existingBookedRoomList.stream().filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }

    private boolean isUpdatePossibleOnSelectedDate(PatchBookingRequest request, LocalDate middleDate) {

        //search for possible existing reservations for all 3 possible dates
        Optional<RoomEntity> firstDayBook = findExistingReservation(request.getNewReservationStartDate());
        Optional<RoomEntity> lastDayBook = findExistingReservation(request.getNewReservationEndDate());
        Optional<RoomEntity> middleDayBook = null == middleDate ? Optional.empty() : findExistingReservation(middleDate);

        List<Optional<RoomEntity>> existingBookedRoomList = Arrays.asList(firstDayBook, middleDayBook, lastDayBook);

        List<RoomEntity> sameTimeReservations = new ArrayList<>();

        //validates if there is a reservation that conflicts with the requested date, and if there is, search if any is from the requester
       existingBookedRoomList.stream().filter(Optional::isPresent).forEach(RoomEntity -> {
            RoomEntity room = RoomEntity.get();
            if (!room.getBookId().equals(request.getBookId())) {
                sameTimeReservations.add(room);
            }
        });

        //if there are no conflicting reservations, or if none of them are from different users, returns true
        return existingBookedRoomList.stream().allMatch(Optional::isEmpty) || sameTimeReservations.isEmpty();
    }

}
