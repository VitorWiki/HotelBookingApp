package com.alten.hotelBooking.utils;

import com.alten.hotelBooking.enums.ErrorEnum;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationDateUtil {

    public static void dateValidations(LocalDate startDate, LocalDate endDate) {

        final LocalDate today = LocalDate.now();

        //Throws error if the end date of booking is before the starting date
        if(endDate.isBefore(startDate)) {
            throw new RuntimeException(ErrorEnum.END_DATE_BEFORE_START_DATE.getDescription());
        }

        //Throws error if the user tries to make a reservation longer than 3 days
        if(daysReserved(startDate, endDate) > 2) {
            throw new RuntimeException(ErrorEnum.RESERVATION_TIME_LONGER_THAN_3_DAYS.getDescription());
        }

        //Throws error if the user tries a reservation in the same day or previous day of booking
        if (today.isEqual(startDate) || startDate.isBefore(today)) {
            throw new RuntimeException(ErrorEnum.RESERVATION_TODAY.getDescription());
        }

        //Throws error if the user tries to reserve the room with more than 30 days of antecedence
        if(daysReserved(today, startDate) > 30) {
            throw new RuntimeException(ErrorEnum.RESERVATION_AFTER_30_DAYS.getDescription());
        }

    }

    //In case the user wants to stay for 3 days, finds the middle day of the reservation
    public static LocalDate getMiddleDate(LocalDate startDate, LocalDate endDate) {

        if (daysReserved(startDate, endDate) == 2) {
            return startDate.plusDays(1);
        }
        return null;
    }

    //In case the user wants to stay for 3 days, finds the middle day of the reservation
    private static long daysReserved(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
