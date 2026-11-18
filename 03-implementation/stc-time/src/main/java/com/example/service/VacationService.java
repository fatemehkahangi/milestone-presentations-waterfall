package com.example.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.domain.*;
import com.example.repository.*;

@Service
public class VacationService {

    public static final int ANNUAL_DAYS = 30;

    /** Vacation days of one year: taken (past), planned (approved, future), pending (requested), open (left). */
    public record Summary(int taken, int planned, int pending, int open) {}

    private final VacationRequestRepository requests;
    private final UserRepository users;
    private final NotificationService notifications;

    public VacationService(VacationRequestRepository requests, UserRepository users,
                           NotificationService notifications) {
        this.requests = requests;
        this.users = users;
        this.notifications = notifications;
    }

    /** Monday to Friday days between start and end, inclusive. 0 if the input is invalid. */
    public static int countWorkingDays(LocalDate start, LocalDate end) {
        if (start == null || end == null || end.isBefore(start)) {
            return 0;
        }
        int days = 0;
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            if (d.getDayOfWeek().getValue() <= 5) {
                days++;
            }
        }
        return days;
    }

    @Transactional(readOnly = true)
    public Summary summary(User employee, int year) {
        int taken = 0, planned = 0, pending = 0;
        LocalDate today = LocalDate.now();
        for (VacationRequest r : requests.findByEmployeeOrderByStartDateDesc(employee)) {
            if (r.getStartDate().getYear() != year) {
                continue;
            }
            if (r.getStatus() == VacationStatus.REQUESTED) {
                pending += r.getWorkingDays();
            } else if (r.getStatus() == VacationStatus.APPROVED) {
                if (r.getEndDate().isBefore(today)) {
                    taken += r.getWorkingDays();
                } else {
                    planned += r.getWorkingDays();
                }
            }
        }
        return new Summary(taken, planned, pending, ANNUAL_DAYS - taken - planned - pending);
    }

    /** Creates a request (status REQUESTED) and notifies the supervisor. */
    @Transactional
    public VacationRequest request(User employee, LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end date are required");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date must not be before the start date");
        }
        if (!start.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Vacation must be requested at least 1 day in advance");
        }
        if (start.getYear() != end.getYear()) {
            throw new IllegalArgumentException("A request must stay within one calendar year");
        }
        int days = countWorkingDays(start, end);
        if (days == 0) {
            throw new IllegalArgumentException("The selected period has no working days");
        }
        for (VacationRequest r : requests.findByEmployeeOrderByStartDateDesc(employee)) {
            boolean active = r.getStatus() == VacationStatus.REQUESTED
                    || r.getStatus() == VacationStatus.APPROVED;
            boolean overlap = !r.getStartDate().isAfter(end) && !r.getEndDate().isBefore(start);
            if (active && overlap) {
                throw new IllegalArgumentException("The period overlaps with an existing vacation");
            }
        }
        int open = summary(employee, start.getYear()).open();
        if (days > open) {
            throw new IllegalArgumentException("Not enough vacation days: " + open + " left, "
                    + days + " requested");
        }

        VacationRequest r = new VacationRequest();
        r.setEmployee(employee);
        r.setStartDate(start);
        r.setEndDate(end);
        r.setWorkingDays(days);
        r.setStatus(VacationStatus.REQUESTED);
        VacationRequest saved = requests.save(r);

        if (employee.getSupervisor() != null) {
            notifications.notify(employee.getSupervisor(), employee.getName()
                    + " requested vacation from " + start + " to " + end + " (" + days + " days)",
                    "vacation-approvals");
        }
        return saved;
    }

    /** REQUESTED -> APPROVED. Notifies the employee and all HR users. */
    @Transactional
    public void approve(Long requestId) {
        VacationRequest r = load(requestId);
        requireRequested(r);
        r.setStatus(VacationStatus.APPROVED);
        requests.save(r);

        String text = "Vacation " + r.getStartDate() + " to " + r.getEndDate() + " of "
                + r.getEmployee().getName() + " was approved";
        notifications.notify(r.getEmployee(), text, "vacation");
        notifyHr(text);
    }

    /** REQUESTED -> REJECTED with a reason. Notifies the employee and all HR users. */
    @Transactional
    public void reject(Long requestId, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("A reason is required");
        }
        VacationRequest r = load(requestId);
        requireRequested(r);
        r.setStatus(VacationStatus.REJECTED);
        r.setRejectionReason(reason.trim());
        requests.save(r);

        String text = "Vacation " + r.getStartDate() + " to " + r.getEndDate() + " of "
                + r.getEmployee().getName() + " was rejected: " + r.getRejectionReason();
        notifications.notify(r.getEmployee(), text, "vacation");
        notifyHr(text);
    }

    /** Employee deletes an open request or an approved future vacation. Notifies the supervisor. */
    @Transactional
    public void cancel(Long requestId) {
        VacationRequest r = load(requestId);
        boolean cancelable = r.getStatus() == VacationStatus.REQUESTED
                || r.getStatus() == VacationStatus.APPROVED;
        if (!cancelable || !r.getStartDate().isAfter(LocalDate.now())) {
            throw new IllegalStateException("Only open requests and future vacation can be deleted");
        }
        r.setStatus(VacationStatus.CANCELED);
        requests.save(r);

        User supervisor = r.getEmployee().getSupervisor();
        if (supervisor != null) {
            notifications.notify(supervisor, r.getEmployee().getName() + " deleted the vacation "
                    + r.getStartDate() + " to " + r.getEndDate(), "vacation-approvals");
        }
    }

    @Transactional(readOnly = true)
    public List<VacationRequest> listFor(User employee) {
        return requests.findByEmployeeOrderByStartDateDesc(employee);
    }

    @Transactional(readOnly = true)
    public List<VacationRequest> findPendingForSupervisor(User supervisor) {
        return requests.findByEmployee_SupervisorAndStatus(supervisor, VacationStatus.REQUESTED);
    }

    private VacationRequest load(Long id) {
        return requests.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
    }

    private void requireRequested(VacationRequest r) {
        if (r.getStatus() != VacationStatus.REQUESTED) {
            throw new IllegalStateException("The request is already " + r.getStatus());
        }
    }

    private void notifyHr(String text) {
        for (User u : users.findAll()) {
            if (u.getRole() == Role.HR) {
                notifications.notify(u, text);
            }
        }
    }
}