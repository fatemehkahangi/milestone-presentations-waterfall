package com.example.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class TimeEntryTest {

    @Test
    public void testGetWorkedMinutes() {
        TimeEntry entry = new TimeEntry();
        entry.setBeginTime(LocalTime.of(9, 0));
        entry.setEndTime(LocalTime.of(17, 30));
        entry.setBreakMinutes(30);

        assertEquals(480, entry.getWorkedMinutes());
    }

    @Test
    public void testGetWorkedMinutesNoTimes() {
        TimeEntry entry = new TimeEntry();
        assertEquals(0, entry.getWorkedMinutes());
    }
}