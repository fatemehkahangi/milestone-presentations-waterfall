package com.example.ui;

import com.example.domain.Notification;
import com.example.domain.User;
import com.example.security.AuthService;
import com.example.service.FlexTimeService;
import com.example.service.NotificationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | STC Time")
@PermitAll
public class DashboardView extends VerticalLayout {

    private final AuthService authService;
    private final FlexTimeService flexTimeService;
    private final NotificationService notificationService;

    private Grid<Notification> notificationGrid;
    private Span unreadCountSpan;
    private Span flexBalanceSpan;

    public DashboardView(AuthService authService,
                         FlexTimeService flexTimeService,
                         NotificationService notificationService) {
        this.authService = authService;
        this.flexTimeService = flexTimeService;
        this.notificationService = notificationService;

        setSpacing(true);
        setPadding(true);

        // Get currently authenticated user
        User currentUser = authService.getCurrentUser();

        // 1. Greeting header
        H1 greeting = new H1("Welcome, " + (currentUser != null ? currentUser.getName() : "User") + "!");
        add(greeting);

        // 2. Flex time account card
        add(createFlexTimeCard(currentUser));

        // 3. Notifications section
        add(createNotificationsSection(currentUser));
    }

    // Creates the card displaying the user's flex time balance
    private VerticalLayout createFlexTimeCard(User user) {
        VerticalLayout card = new VerticalLayout();
        card.setClassName("flex-time-card");
        card.getStyle().set("background-color", "#f4f5f7");
        card.getStyle().set("padding", "15px");
        card.getStyle().set("border-radius", "8px");

        H3 title = new H3("Flex time account");

        flexBalanceSpan = new Span();
        updateFlexBalance(user);

        card.add(title, flexBalanceSpan);
        return card;
    }

    // Safely updates the flex balance value (wrapped in try-catch for service stubs)
    private void updateFlexBalance(User user) {
        try {
            int balanceMinutes = flexTimeService.getBalanceMinutes(user);
            flexBalanceSpan.setText(formatSignedMinutes(balanceMinutes));
        } catch (Exception e) {
            flexBalanceSpan.setText("N/A (Loading...)");
        }
    }

    // Creates the notifications section layout and components
    private VerticalLayout createNotificationsSection(User user) {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setAlignItems(Alignment.CENTER);

        H3 title = new H3("Notifications");
        unreadCountSpan = new Span();
        updateUnreadCountBadge(user);

        // Button to mark all notifications as read
        Button markAllReadBtn = new Button("Mark all as read", e -> {
            if (user != null) {
                notificationService.markAllRead(user);
                refreshData(user);
            }
        });

        headerLayout.add(title, unreadCountSpan, markAllReadBtn);

        // Grid to display notifications
        notificationGrid = new Grid<>(Notification.class, false);
        notificationGrid.addColumn(Notification::getMessage).setHeader("Message");
        notificationGrid.addColumn(Notification::getCreatedAt).setHeader("Time");
        notificationGrid.addColumn(n -> n.isRead() ? "Read" : "Unread").setHeader("Status");

        refreshGridData(user);

        section.add(headerLayout, notificationGrid);
        return section;
    }

    // Updates the unread notification count label
    private void updateUnreadCountBadge(User user) {
        if (user != null) {
            long count = notificationService.unreadCount(user);
            unreadCountSpan.setText("(" + count + " unread)");
        }
    }

    // Refreshes the grid items from the database
    private void refreshGridData(User user) {
        if (user != null) {
            notificationGrid.setItems(notificationService.listFor(user));
        }
    }

    // Refreshes all dashboard data at once
    private void refreshData(User user) {
        updateFlexBalance(user);
        updateUnreadCountBadge(user);
        refreshGridData(user);
    }

    // Formats minutes into signed text representation (e.g., +1h 30min or -0h 45min)
    private String formatSignedMinutes(int minutes) {
        int hours = Math.abs(minutes) / 60;
        int rem = Math.abs(minutes) % 60;
        String formatted = hours + "h " + (rem < 10 ? "0" : "") + rem + "min";
        if (minutes > 0) {
            return "+" + formatted;
        } else if (minutes < 0) {
            return "-" + formatted;
        }
        return "0h 00min";
    }
}