package com.alten.hotelBooking.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class ReservationDateUtilTest {

    @Test
    public void validate_user_dates_throws_error_for_enddate_before_startdate() {

        Throwable exception = Assertions.assertThrows(RuntimeException.class, () ->
                ReservationDateUtil.dateValidations(LocalDate.now().plusDays(3), LocalDate.now().plusDays(1)));

        Assertions.assertEquals(exception.getMessage(), "end date must be after start date");
    }

    @Test
    public void validate_user_dates_throws_error_for_reservation_period_longer_than_3_days() {

        Throwable exception = Assertions.assertThrows(RuntimeException.class, () ->
                ReservationDateUtil.dateValidations(LocalDate.now().plusDays(1), LocalDate.now().plusDays(4)));

        Assertions.assertEquals(exception.getMessage(), "number of days for booking can't pass 3 days");

    }

    @Test
    public void validate_user_dates_throws_error_for_startday_equal_current_day() {

        Throwable exception = Assertions.assertThrows(RuntimeException.class, () ->
                ReservationDateUtil.dateValidations(LocalDate.now(), LocalDate.now().plusDays(1)));

        Assertions.assertEquals(exception.getMessage(), "reservations can only start tomorrow onwards");

    }

    @Test
    public void validate_user_dates_throws_error_for_startday_before_current_day() {

        Throwable exception = Assertions.assertThrows(RuntimeException.class, () ->
                ReservationDateUtil.dateValidations(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)));

        Assertions.assertEquals(exception.getMessage(), "reservations can only start tomorrow onwards");

    }

    @Test
    public void validate_user_dates_throws_error_for_startdate_longer_than_30_days_in_the_future() {

        Throwable exception = Assertions.assertThrows(RuntimeException.class, () ->
                ReservationDateUtil.dateValidations(LocalDate.now().plusDays(40), LocalDate.now().plusDays(41)));

        Assertions.assertEquals(exception.getMessage(), "reservations can only be made up until 30 days in the future");

    }

    @Test
    public void get_middle_date(){

        LocalDate middle = ReservationDateUtil.getMiddleDate(LocalDate.now(), LocalDate.now().plusDays(2));

        Assertions.assertEquals(middle, LocalDate.now().plusDays(1));
    }

    @Test
    public void get_middle_date_null(){

        LocalDate middle = ReservationDateUtil.getMiddleDate(LocalDate.now(), LocalDate.now().plusDays(1));

        Assertions.assertNull(middle);
    }


}
