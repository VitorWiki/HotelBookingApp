package com.alten.hotelBooking.utils;

import com.alten.hotelBooking.enums.ErrorEnum;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static void dateValidations(LocalDate startDate, LocalDate endDate) {

        final LocalDate today = LocalDate.now();

        if(endDate.isBefore(startDate)) {
            throw new RuntimeException(ErrorEnum.END_DATE_BEFORE_START_DATE.getDescription());
        }

        if(daysReserved(startDate, endDate) > 2) {
            throw new RuntimeException(ErrorEnum.RESERVATION_TIME_LONGER_THAN_3_DAYS.getDescription());
        }

        if (today.isEqual(startDate) || startDate.isBefore(today)) {
            throw new RuntimeException(ErrorEnum.RESERVATION_TODAY.getDescription());
        }

        if(daysReserved(today, startDate) > 30) {
            throw new RuntimeException(ErrorEnum.RESERVATION_AFTER_30_DAYS.getDescription());
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
