package com.example.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.domain.*;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findByEmployeeOrderByStartDateDesc(User employee);
    List<Absence> findAllByOrderByStartDateDesc();
}