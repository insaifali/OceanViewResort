package com.oceanview.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class DateUtil {
    private DateUtil(){}

    public static int nights(LocalDate checkIn, LocalDate checkOut) {
        long n = ChronoUnit.DAYS.between(checkIn, checkOut);
        return (int) n;
    }
}