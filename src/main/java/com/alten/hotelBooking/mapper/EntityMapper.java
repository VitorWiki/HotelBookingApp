package com.alten.hotelBooking.mapper;

import com.alten.hotelBooking.controller.request.PatchBookingRequest;
import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.controller.response.GetBookingResponse;
import com.alten.hotelBooking.controller.response.PatchBookingResponse;
import com.alten.hotelBooking.repositories.entities.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.time.LocalDate;

@Mapper
public interface EntityMapper {

    String ROOM_NOT_AVAILABLE = "Room not available for booking on this day";

    RoomEntity mapPostFrom(PostBookingRequest request, LocalDate reservationMiddleDate);

    @Mapping(target = "reservationStartDate", source = "request.newReservationStartDate")
    @Mapping(target = "reservationEndDate", source = "request.newReservationEndDate")
    @Mapping(target = "reservationMiddleDate", source = "reservationMiddleDate")
    @Mapping(target = "bookId", source = "previousRoom.bookId")
    RoomEntity mapPatchFrom(PatchBookingRequest request, RoomEntity previousRoom, LocalDate reservationMiddleDate);

    PatchBookingResponse mapPatchFromEntity(RoomEntity entity);

    @Mapping(target = "statusMessage", constant = ROOM_NOT_AVAILABLE)
    GetBookingResponse mapGetFromExistingEntity(RoomEntity entity);
}
