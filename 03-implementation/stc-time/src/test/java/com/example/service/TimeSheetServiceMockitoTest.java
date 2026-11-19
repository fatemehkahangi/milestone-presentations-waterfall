package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.domain.*;
import com.example.repository.TimeSheetRepository;

@ExtendWith(MockitoExtension.class)
class TimeSheetServiceMockitoTest {

    @Mock private TimeSheetRepository sheets;
    @Mock private FlexTimeService flexTime;
    @Mock private NotificationService notifications;

    @InjectMocks private TimeSheetService service;

    @Test
    void approve_updatesStatusAndCallsFlexTime() {
        TimeSheet sheet = new TimeSheet();
        sheet.setStatus(TimeSheetStatus.SUBMITTED);
        when(sheets.findById(1L)).thenReturn(Optional.of(sheet));

        service.approve(1L);

        assertEquals(TimeSheetStatus.APPROVED, sheet.getStatus());
        verify(flexTime).applyApprovedSheet(sheet);
        verify(sheets).save(sheet);
    }
}
