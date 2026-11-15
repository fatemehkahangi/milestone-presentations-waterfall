package com.example.service;

import java.util.Optional;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
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

	@Transactional
	public void submit(Long sheetId) {
	
	    // 1. Find the time sheet
	    Optional<TimeSheet> result = sheets.findById(sheetId);
	
	    if (result.isEmpty()) {
	        throw new IllegalArgumentException("Time sheet not found");
	    }
	
	    TimeSheet sheet = result.get();
	
	    // 2. Check the status
	    if (sheet.getStatus() != TimeSheetStatus.OPEN
	            && sheet.getStatus() != TimeSheetStatus.REJECTED) {
	        throw new IllegalStateException("Cannot submit this time sheet");
	    }
	
	    // 3. Update the status and submission time
	    sheet.setStatus(TimeSheetStatus.SUBMITTED);
	    sheet.setSubmittedAt(LocalDateTime.now());
	    sheet.setRejectionReason(null);
	
	    sheets.save(sheet);
	
	    // 4. Notify the supervisor
	    User supervisor = sheet.getEmployee().getSupervisor();
	
	    if (supervisor != null) {
	        notifications.notify(supervisor,
	                sheet.getEmployee().getName()
	                + " submitted a time sheet");
	    }
	}


    /** SUBMITTED -> APPROVED. Updates the flex time account via flexTime. */

	@Transactional
	public void approve(Long sheetId) {
	
	    // 1. Find the time sheet
	    Optional<TimeSheet> result = sheets.findById(sheetId);
	
	    if (result.isEmpty()) {
	        throw new IllegalArgumentException("Time sheet not found");
	    }
	
	    TimeSheet sheet = result.get();
	
	    // 2. Check the status
	    if (sheet.getStatus() != TimeSheetStatus.SUBMITTED) {
	        throw new IllegalStateException("Cannot approve this time sheet");
	    }
	
	    // 3. Update the status
	    sheet.setStatus(TimeSheetStatus.APPROVED);
	
	    // 4. Update the flex time balance
	    flexTime.applyApprovedSheet(sheet);
	
	    // 5. Save the time sheet
	    sheets.save(sheet);
	}


    /** SUBMITTED -> REJECTED with a reason. Notifies the employee. */

	@Transactional
	public void reject(Long sheetId, String reason) {
	
	    // 1. Find the time sheet
	    Optional<TimeSheet> result = sheets.findById(sheetId);
	
	    if (result.isEmpty()) {
	        throw new IllegalArgumentException("Time sheet not found");
	    }
	
	    TimeSheet sheet = result.get();
	
	    // 2. Check the status
	    if (sheet.getStatus() != TimeSheetStatus.SUBMITTED) {
	        throw new IllegalStateException("Cannot reject this time sheet");
	    }
	
	    // 3. Check the rejection reason
	    if (reason == null || reason.trim().isEmpty()) {
	        throw new IllegalArgumentException("Rejection reason is required");
	    }
	
	    // 4. Update the time sheet
	    sheet.setStatus(TimeSheetStatus.REJECTED);
	    sheet.setRejectionReason(reason);
	
	    sheets.save(sheet);
	
	    // 5. Notify the employee
	    notifications.notify(sheet.getEmployee(),
	            "Your time sheet was rejected: " + reason);
	}

    /** Sheets with status SUBMITTED of all employees of this supervisor. */

	@Transactional(readOnly = true)
	public List<TimeSheet> findPendingForSupervisor(User supervisor) {
	
	    return sheets.findByEmployee_SupervisorAndStatus(
	            supervisor, TimeSheetStatus.SUBMITTED);
	}


    /** True if the sheet may no longer be edited (SUBMITTED or APPROVED). */
    public static boolean isLocked(TimeSheet sheet) {
    	return sheet.getStatus() == TimeSheetStatus.SUBMITTED
    	        || sheet.getStatus() == TimeSheetStatus.APPROVED;
    }
}