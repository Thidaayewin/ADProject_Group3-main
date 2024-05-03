package com.team3.weather.util;

import java.time.LocalDateTime;

public class LDT {
    public static LocalDateTime getCourseStart (Integer year) {
        LocalDateTime dateTime = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        return dateTime;
    }

    public static LocalDateTime getCourseEnd (Integer year) {
        LocalDateTime dateTime = LocalDateTime.of(year+1, 1, 1, 0, 0, 0);
        return dateTime;
    }
}
