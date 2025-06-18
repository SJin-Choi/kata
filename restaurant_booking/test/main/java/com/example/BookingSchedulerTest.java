package com.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingSchedulerTest {

    private final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    private final LocalDateTime NOT_ON_THE_HOUR = LocalDateTime.parse("2021/03/26 09:05", FORMAT);
    private final LocalDateTime ON_THE_HOUR = LocalDateTime.parse("2021/03/26 09:00", FORMAT);
    private final Customer CUSTOMER = new Customer("Fake name", "010-1234-5678");
    private final int UNDER_CAPACITY = 1;
    private final int CAPACITY_PER_HOUR = 3;
    private final TestableBookingScheduler testableBookingScheduler = new TestableBookingScheduler(CAPACITY_PER_HOUR);
    private final Customer customerWithMail = new Customer("Fake Name", "010-1234-5678", "test@tset.com");


    BookingScheduler bookingScheduler;
    TestableSmsSender testableSmsSender = new TestableSmsSender();
    TestableMailSender testableMailSender = new TestableMailSender();

    public BookingSchedulerTest() {
        bookingScheduler = new BookingScheduler(CAPACITY_PER_HOUR);
    }

    @BeforeEach
    void setUp() {
        bookingScheduler.setSmsSender(testableSmsSender);
        bookingScheduler.setMailSender(testableMailSender);
    }

    @Test
    public void 예약은_정시에만_가능하다_정시가_아닌경우_예약불가() {
        Schedule schedule = new Schedule(NOT_ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER);

        assertThrows(RuntimeException.class, () -> {
            bookingScheduler.addSchedule(schedule);
        });
    }

    @Test
    public void 예약은_정시에만_가능하다_정시인_경우_예약가능() {
        Schedule schedule = new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER);
        BookingScheduler bookingScheduler = new BookingScheduler(CAPACITY_PER_HOUR);

        bookingScheduler.addSchedule(schedule);

        assertThat(bookingScheduler.hasSchedule(schedule)).isEqualTo(true);
    }

    @Test
    public void 시간대별_인원제한이_있다_같은_시간대에_Capacity_초과할_경우_예외발생() {
        Schedule schedule = new Schedule(ON_THE_HOUR, CAPACITY_PER_HOUR, CUSTOMER);
        bookingScheduler.addSchedule(schedule);

        try{
            Schedule newSchdule = new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER);
            bookingScheduler.addSchedule(newSchdule);
            Assertions.fail();
        }catch (RuntimeException e){
            assertThat(e.getMessage()).isEqualTo("Number of people is over restaurant capacity per hour");
        }
    }

    @Test
    public void 시간대별_인원제한이_있다_같은_시간대가_다르면_Capacity_차있어도_스케쥴_추가_성공() {
        Schedule schedule = new Schedule(ON_THE_HOUR, CAPACITY_PER_HOUR, CUSTOMER);
        bookingScheduler.addSchedule(schedule);

        LocalDateTime differentHour = ON_THE_HOUR.plusHours(1);
        Schedule newSchedule = new Schedule(differentHour, UNDER_CAPACITY, CUSTOMER);
        bookingScheduler.addSchedule(newSchedule);

        assertThat(bookingScheduler.hasSchedule(schedule)).isEqualTo(true);
    }

    @Test
    public void 예약완료시_SMS는_무조건_발송() {
        Schedule schedule = new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER);

        bookingScheduler.addSchedule(schedule);

        Assertions.assertThat(testableSmsSender.isSendMethodIsCalled()).isEqualTo(true);
    }

    @Test
    public void 이메일이_없는_경우에는_이메일_미발송() {
        Schedule schedule = new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER);

        bookingScheduler.addSchedule(schedule);

        assertThat(testableMailSender.getCountSendMailMethodIsCalled()).isEqualTo(0);
    }

    @Test
    public void 이메일이_있는_경우에는_이메일_발송() {
        Schedule schedule = new Schedule(ON_THE_HOUR, UNDER_CAPACITY, customerWithMail);

        bookingScheduler.addSchedule(schedule);

        assertThat(testableMailSender.getCountSendMailMethodIsCalled()).isEqualTo(1);
    }

    @Test
    public void 현재날짜가_일요일인_경우_예약불가_예외처리() {
        testableBookingScheduler.setWeekDayToSunday();
        Schedule schedule= new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER);
        try {
            testableBookingScheduler.addSchedule(schedule);
            Assertions.fail();
        } catch (RuntimeException e){
            assertThat(e.getMessage()).isEqualTo("Booking system is not available on sunday");
        }
    }

    @Test
    public void 현재날짜가_일요일이_아닌경우_예약가능() {
        testableBookingScheduler.setWeekDayToMonday();

        Schedule schedule= new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER);
        testableBookingScheduler.addSchedule(schedule);

        assertThat(testableBookingScheduler.hasSchedule(schedule)).isEqualTo(true);
    }
}
