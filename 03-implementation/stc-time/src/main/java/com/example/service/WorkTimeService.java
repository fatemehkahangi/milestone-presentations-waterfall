package com.example.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.domain.*;
import com.example.repository.*;

@Service
public class WorkTimeService {

    /** More than 10 hours (600 min) on one day triggers an overtime notice. */
    public static final int DAILY_LIMIT_MINUTES = 600;

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
    @Transactional
    public TimeSheet getOrCreateTimeSheet(User employee, YearMonth month) {
        return sheets.findByEmployeeAndYearAndMonth(
                        employee, month.getYear(), month.getMonthValue())
                .orElseGet(() -> {
                    TimeSheet sheet = new TimeSheet();
                    sheet.setEmployee(employee);
                    sheet.setYear(month.getYear());
                    sheet.setMonth(month.getMonthValue());
                    sheet.setStatus(TimeSheetStatus.OPEN);
                    return sheets.save(sheet);
                });
    }

    /**
     * Adds an entry. Rejects it (IllegalArgumentException with a readable message) if the
     * sheet is not OPEN or REJECTED, end is before begin, or the break rule is violated.
     * If the day's total exceeds 10 hours, calls notifications.notifyOvertime(...).
     */
    @Transactional
    public TimeEntry addEntry(User employee, LocalDate date, LocalTime begin, LocalTime end,
                              int breakMinutes, Project project, String comment) {

        // 1. Required fields
        if (date == null || begin == null || end == null) {
            throw new IllegalArgumentException("Date, begin and end are required");
        }

        // 2. The sheet of that month must still be editable
        TimeSheet sheet = getOrCreateTimeSheet(employee, YearMonth.from(date));
        if (TimeSheetService.isLocked(sheet)) {
            throw new IllegalArgumentException(
                    "This time sheet is " + sheet.getStatus() + " and can no longer be edited");
        }

        // 3. Times and break
        if (!end.isAfter(begin)) {
            throw new IllegalArgumentException("End must be after begin");
        }
        if (breakMinutes < 0) {
            throw new IllegalArgumentException("Break cannot be negative");
        }

        int workedBeforeBreak = (int) Duration.between(begin, end).toMinutes();
        if (breakMinutes >= workedBeforeBreak) {
            throw new IllegalArgumentException("Break is longer than the working time");
        }
        if (!isBreakValid(workedBeforeBreak, breakMinutes)) {
            throw new IllegalArgumentException("Break too short: at least "
                    + requiredBreakMinutes(workedBeforeBreak) + " min required for "
                    + workedBeforeBreak / 60 + "h " + workedBeforeBreak % 60 + "min of work");
        }

        // 4. Save the entry
        TimeEntry entry = new TimeEntry();
        entry.setTimeSheet(sheet);
        entry.setDate(date);
        entry.setBeginTime(begin);
        entry.setEndTime(end);
        entry.setBreakMinutes(breakMinutes);
        entry.setProject(project);
        entry.setComment(comment == null || comment.isBlank() ? null : comment.trim());
        TimeEntry saved = entries.save(entry);

        // 5. More than 10 hours on that day -> notify the supervisor
        int dayTotal = dailyWorkedMinutes(sheet, date);
        if (dayTotal > DAILY_LIMIT_MINUTES) {
            try {
                notifications.notifyOvertime(employee, date, dayTotal);
            } catch (UnsupportedOperationException e) {
                // NotificationService is still a stub (person 4).
                // Remove this try/catch once notifyOvertime is implemented.
                System.out.println("[OVERTIME not sent yet] " + employee.getName()
                        + " worked " + dayTotal + " min on " + date);
            }
        }

        return saved;
    }

    /** Deletes an entry. Refuses if its time sheet is locked (SUBMITTED or APPROVED). */
    @Transactional
    public void deleteEntry(Long entryId) {
        TimeEntry entry = entries.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("Entry not found"));

        if (TimeSheetService.isLocked(entry.getTimeSheet())) {
            throw new IllegalArgumentException("This time sheet is locked, entries cannot be deleted");
        }

        entries.delete(entry);
    }

    /** All entries of the sheet, sorted by date and begin time. */
    @Transactional(readOnly = true)
    public List<TimeEntry> getEntries(TimeSheet sheet) {
        return entries.findByTimeSheetOrderByDateAscBeginTimeAsc(sheet);
    }

    /** Total worked minutes of one day (all entries of that date). */
    @Transactional(readOnly = true)
    public int dailyWorkedMinutes(TimeSheet sheet, LocalDate date) {
        int total = 0;
        for (TimeEntry entry : entries.findByTimeSheetAndDate(sheet, date)) {
            total += entry.getWorkedMinutes();
        }
        return total;
    }

    /** Break rule: up to 6 h -> 0 min, over 6 h -> 30 min, over 9 h -> 60 min. */
    public static int requiredBreakMinutes(int workedMinutesBeforeBreak) {
        if (workedMinutesBeforeBreak > 540) {
            return 60;
        }
        if (workedMinutesBeforeBreak > 360) {
            return 30;
        }
        return 0;
    }

    public static boolean isBreakValid(int workedMinutesBeforeBreak, int breakMinutes) {
        return breakMinutes >= requiredBreakMinutes(workedMinutesBeforeBreak);
    }
}
