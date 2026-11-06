package com.example.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.domain.*;

public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {
    Optional<TimeSheet> findByEmployeeAndYearAndMonth(User employee, int year, int month);
    List<TimeSheet> findByEmployeeOrderByYearDescMonthDesc(User employee);
    List<TimeSheet> findByEmployee_SupervisorAndStatus(User supervisor, TimeSheetStatus status);
}