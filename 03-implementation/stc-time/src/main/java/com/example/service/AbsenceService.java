package com.example.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.domain.*;
import com.example.repository.*;

@Service
public class AbsenceService {

    private final AbsenceRepository absences;
    private final UserRepository users;
    private final NotificationService notifications;

    public AbsenceService(AbsenceRepository absences, UserRepository users,
                          NotificationService notifications) {
        this.absences = absences;
        this.users = users;
        this.notifications = notifications;
    }

    /** HR registers sickness for an employee. No approval needed. Notifies the supervisor. */
    @Transactional
    public Absence registerSickness(User hr, Long employeeId, LocalDate start, LocalDate end) {
        if (hr == null || hr.getRole() != Role.HR) {
            throw new IllegalStateException("Only HR can register sickness");
        }
        if (employeeId == null || start == null || end == null) {
            throw new IllegalArgumentException("Employee, start and end date are required");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date must not be before the start date");
        }
        User employee = users.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Absence a = new Absence();
        a.setEmployee(employee);
        a.setStartDate(start);
        a.setEndDate(end);
        Absence saved = absences.save(a);

        if (employee.getSupervisor() != null) {
            notifications.notify(employee.getSupervisor(),
                    employee.getName() + " is sick from " + start + " to " + end);
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Absence> listFor(User employee) {
        return absences.findByEmployeeOrderByStartDateDesc(employee);
    }

    @Transactional(readOnly = true)
    public List<Absence> listAll() {
        return absences.findAllByOrderByStartDateDesc();
    }

    /** All active users except the admin, for the HR drop-down. */
    @Transactional(readOnly = true)
    public List<User> listEmployees() {
        return users.findAll().stream()
                .filter(User::isActive)
                .filter(u -> u.getRole() != Role.ADMIN)
                .toList();
    }
}