package com.example.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.domain.*;
import com.example.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notifications;

    public NotificationService(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    /** Saves a notification and prints it to the log as a fake email. */
    public void notify(User recipient, String message) {
        throw new UnsupportedOperationException("TODO person 4");
    }

    /** Sends to employee's supervisor only (not HR, not the employee). */
    public void notifyOvertime(User employee, LocalDate date, int workedMinutes) {
        throw new UnsupportedOperationException("TODO person 4");
    }

    public List<Notification> listFor(User user) {
        throw new UnsupportedOperationException("TODO person 4");
    }

    public long unreadCount(User user) {
        throw new UnsupportedOperationException("TODO person 4");
    }

    public void markAllRead(User user) {
        throw new UnsupportedOperationException("TODO person 4");
    }
}