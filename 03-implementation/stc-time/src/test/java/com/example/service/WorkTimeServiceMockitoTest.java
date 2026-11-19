package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.YearMonth;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.domain.*;
import com.example.repository.TimeEntryRepository;
import com.example.repository.TimeSheetRepository;

@ExtendWith(MockitoExtension.class)
class WorkTimeServiceMockitoTest {

    @Mock private TimeSheetRepository sheets;
    @Mock private TimeEntryRepository entries;
    @Mock private NotificationService notifications;

    @InjectMocks private WorkTimeService service;

    @Test
    void getOrCreateTimeSheet_createsWhenMissing() {
        User user = new User();
        when(sheets.findByEmployeeAndYearAndMonth(user, 2026, 10)).thenReturn(Optional.empty());
        when(sheets.save(any())).thenAnswer(i -> i.getArgument(0));

        TimeSheet result = service.getOrCreateTimeSheet(user, YearMonth.of(2026, 10));

        assertEquals(TimeSheetStatus.OPEN, result.getStatus());
        verify(sheets).save(any());
    }
}