package com.example.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.domain.*;

public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {
    List<TimeEntry> findByTimeSheetOrderByDateAscBeginTimeAsc(TimeSheet timeSheet);
    List<TimeEntry> findByTimeSheetAndDate(TimeSheet timeSheet, LocalDate date);
}