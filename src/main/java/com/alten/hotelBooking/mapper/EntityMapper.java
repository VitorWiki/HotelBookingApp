package com.alten.hotelBooking.mapper;

import com.alten.hotelBooking.controller.request.PostBookingRequest;
import com.alten.hotelBooking.repositories.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper
public interface EntityMapper {

    //@Mapping(target = "", source = "")
    RoomEntity mapPostFrom(PostBookingRequest request, LocalDate reservationMiddle);
}
