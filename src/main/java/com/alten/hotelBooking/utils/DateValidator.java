package com.alten.hotelBooking.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateValidator {

    public static void dateValidations(LocalDate startDate, LocalDate endDate, LocalDate middleDate) {

        final LocalDate today = LocalDate.now();

        if(daysReserved(startDate, endDate) > 3) {
            throw new RuntimeException("number of days for booking can't pass 3 days");
        }

        if(endDate.isBefore(startDate)) {
            throw new RuntimeException("end date must be after start date");
        }

        if(ChronoUnit.DAYS.between(startDate, LocalDate.now()) > 30) {
            throw new RuntimeException("reservations can only be made up until 30 days in the future");
        }

        if (today.isEqual(startDate) ||
                today.isEqual(endDate) ||
                today.isEqual(middleDate)) {
            throw new RuntimeException("reservations can't be made for the same day");
        }

    }

    public static LocalDate getMiddleDate(LocalDate startDate, LocalDate endDate) {

        if (daysReserved(startDate, endDate) == 3) {
            return startDate.plusDays(1);
        }
        return null;
    }

    private static long daysReserved(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
