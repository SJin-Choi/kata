package com.example;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MondayBookingScheduler extends BookingScheduler{

    public MondayBookingScheduler(int capacityPerHour) {
        super(capacityPerHour);
    }

    @Override
    public LocalDateTime getNow() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return LocalDateTime.parse("2025/06/23 17:00", format);
    }
}
