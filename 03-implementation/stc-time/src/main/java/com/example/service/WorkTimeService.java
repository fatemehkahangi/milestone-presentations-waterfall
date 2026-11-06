package com.example.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.domain.*;
import com.example.repository.*;

@Service
public class WorkTimeService {

    private final TimeSheetRepository sheets;
    private final TimeEntryRepository entries;
    private final NotificationService notifications;

    public WorkTimeService(TimeSheetRepository sheets, TimeEntryRepository entries,
                           NotificationService notifications) {
        this.sheets = sheets;
        this.entries = entries;
        this.notifications = notifications;
    }

    /** Returns the sheet of that month, creates an OPEN one if none exists. */
    public TimeSheet getOrCreateTimeSheet(User employee, YearMonth month) {
        throw new UnsupportedOperationException("TODO person 2");
    }

    /**
     * Adds an entry. Rejects it (IllegalArgumentException with a readable message) if the
     * sheet is not OPEN or REJECTED, end is before begin, or the break rule is violated.
     * If the day's total exceeds 10 hours, calls notifications.notifyOvertime(...).
     */
    public TimeEntry addEntry(User employee, LocalDate date, LocalTime begin, LocalTime end,
                              int breakMinutes, Project project, String comment) {
        throw new UnsupportedOperationException("TODO person 2");
    }

    public void deleteEntry(Long entryId) {
        throw new UnsupportedOperationException("TODO person 2");
    }

    public List<TimeEntry> getEntries(TimeSheet sheet) {
        throw new UnsupportedOperationException("TODO person 2");
    }

    /** Total worked minutes of one day (all entries of that date). */
    public int dailyWorkedMinutes(TimeSheet sheet, LocalDate date) {
        throw new UnsupportedOperationException("TODO person 2");
    }

    /** Break rule: up to 6 h -> 0 min, over 6 h -> 30 min, over 9 h -> 60 min. */
    public static int requiredBreakMinutes(int workedMinutesBeforeBreak) {
        throw new UnsupportedOperationException("TODO person 2");
    }

    public static boolean isBreakValid(int workedMinutesBeforeBreak, int breakMinutes) {
        throw new UnsupportedOperationException("TODO person 2");
    }
}