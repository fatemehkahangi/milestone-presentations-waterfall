package com.example.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.domain.*;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
    List<VacationRequest> findByEmployeeOrderByStartDateDesc(User employee);
    List<VacationRequest> findByEmployee_SupervisorAndStatus(User supervisor, VacationStatus status);
}