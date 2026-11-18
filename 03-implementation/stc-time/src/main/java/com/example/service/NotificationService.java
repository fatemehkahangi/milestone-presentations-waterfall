package com.example.service;

import com.example.domain.Notification;
import com.example.domain.User;
import com.example.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Creates a notification for the recipient, saves it to the database,
     * and prints a fake email simulation to the console.
     */
    public void notify(User recipient, String message) {
        notify(recipient, message, null);
    }

    public void notify(User recipient, String message, String link) {
        if (recipient == null) {
            return;
        }
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setLink(link);
        notificationRepository.save(notification);

        System.out.println("[EMAIL to " + recipient.getEmail() + "] " + message);
    }

    /**
     * Sends an overtime notification (>10 hours) exclusively to the supervisor.
     */
    public void notifyOvertime(User employee, LocalDate date, int minutes) {
        if (employee == null || employee.getSupervisor() == null) {
            return;
        }

        int hours = minutes / 60;
        int remMinutes = minutes % 60;
        String timeFormatted = hours + "h " + remMinutes + "min";
        String formattedDate = date.format(DateTimeFormatter.ISO_DATE);

        String message = employee.getName() + " worked " + timeFormatted + " on " + formattedDate;

        notify(employee.getSupervisor(), message, "approvals");
    }

    /**
     * Returns a list of all notifications for a specific user (sorted by newest first).
     */
    public List<Notification> listFor(User user) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
    }

    /**
     * Returns the count of unread notifications for a specific user.
     */
    public long unreadCount(User user) {
        return notificationRepository.countByRecipientAndReadFalse(user);
    }

    /**
     * Marks all notifications for a specific user as read.
     */
    public void markAllRead(User user) {
        List<Notification> notifications = notificationRepository.findByRecipient(user);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }
}