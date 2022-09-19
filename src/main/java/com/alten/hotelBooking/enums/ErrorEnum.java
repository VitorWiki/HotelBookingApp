package com.alten.hotelBooking.enums;

import lombok.Getter;


public enum ErrorEnum {

    END_DATE_BEFORE_START_DATE("end date must be after start date"),
    RESERVATION_TIME_LONGER_THAN_3_DAYS("number of days for booking can't pass 3 days"),
    RESERVATION_TODAY("reservations can only start tomorrow onwards"),
    RESERVATION_AFTER_30_DAYS("reservations can only be made up until 30 days in the future"),
    ROOM_ALREADY_BOOKED("Room already booked for this date"),
    NO_RESERVATION_FOUND("No reservation found with this id");
    @Getter
    private String description;

    ErrorEnum(String description) { this.description = description; }
}
