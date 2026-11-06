package com.example.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.domain.*;
import com.example.repository.*;

@Service
public class TimeSheetService {

    private final TimeSheetRepository sheets;
    private final FlexTimeService flexTime;
    private final NotificationService notifications;

    public TimeSheetService(TimeSheetRepository sheets, FlexTimeService flexTime,
                            NotificationService notifications) {
        this.sheets = sheets;
        this.flexTime = flexTime;
        this.notifications = notifications;
    }

    /** OPEN or REJECTED -> SUBMITTED. Locks the sheet, notifies the supervisor. */
    public void submit(Long sheetId) {
        throw new UnsupportedOperationException("TODO person 3");
    }

    /** SUBMITTED -> APPROVED. Updates the flex time account via flexTime. */
    public void approve(Long sheetId) {
        throw new UnsupportedOperationException("TODO person 3");
    }

    /** SUBMITTED -> REJECTED with a reason. Notifies the employee. */
    public void reject(Long sheetId, String reason) {
        throw new UnsupportedOperationException("TODO person 3");
    }

    /** Sheets with status SUBMITTED of all employees of this supervisor. */
    public List<TimeSheet> findPendingForSupervisor(User supervisor) {
        throw new UnsupportedOperationException("TODO person 3");
    }

    /** True if the sheet may no longer be edited (SUBMITTED or APPROVED). */
    public static boolean isLocked(TimeSheet sheet) {
        throw new UnsupportedOperationException("TODO person 3");
    }
}