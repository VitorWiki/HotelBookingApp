package com.alten.hotelBooking.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateValidator {

    public static void dateValidations(LocalDate startDate, LocalDate endDate) {

        final LocalDate today = LocalDate.now();

        if(endDate.isBefore(startDate)) {
            throw new RuntimeException("end date must be after start date");
        }

        if(daysReserved(startDate, endDate) > 2) {
            throw new RuntimeException("number of days for booking can't pass 3 days");
        }

        if (today.isEqual(startDate) || startDate.isBefore(today)) {
            throw new RuntimeException("reservations can only start tommorow onwards");
        }

        if(daysReserved(today, startDate) > 30) {
            throw new RuntimeException("reservations can only be made up until 30 days in the future");
        }

    }

    public static LocalDate getMiddleDate(LocalDate startDate, LocalDate endDate) {

        if (daysReserved(startDate, endDate) == 2) {
            return startDate.plusDays(1);
        }
        return null;
    }

    private static long daysReserved(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
