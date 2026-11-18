package com.example.service; 
 
import static org.junit.jupiter.api.Assertions.*; 
import org.junit.jupiter.api.Test; 
import com.example.domain.*; 
 
class TimeSheetServiceTest { 
    private TimeSheet sheet(TimeSheetStatus s) { TimeSheet t = new TimeSheet(); 
t.setStatus(s); return t; } 
 
    @Test void editable() { 
        assertFalse(TimeSheetService.isLocked(sheet(TimeSheetStatus.OPEN))); 
        assertFalse(TimeSheetService.isLocked(sheet(TimeSheetStatus.REJECTED))); 
    } 
    @Test void locked() { 
        assertTrue(TimeSheetService.isLocked(sheet(TimeSheetStatus.SUBMITTED))); 
        assertTrue(TimeSheetService.isLocked(sheet(TimeSheetStatus.APPROVED))); 
    } 
}