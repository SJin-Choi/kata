package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestableBookingScheduler extends BookingScheduler{
    LocalDateTime innerTime;
    public TestableBookingScheduler(int capacityPerHour) {
        super(capacityPerHour);
        this.innerTime = LocalDateTime.now();
    }

    public void setWeekDayToMonday(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        innerTime = LocalDateTime.parse("2025/06/23 17:00", format);
    }

    public void setWeekDayToSunday(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        innerTime = LocalDateTime.parse("2025/06/22 17:00", format);
    }

    @Override
    public LocalDateTime getNow() {
        return innerTime;
    }
}
