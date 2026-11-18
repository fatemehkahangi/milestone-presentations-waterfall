package com.example.ui;

import java.time.format.DateTimeFormatter;

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

import com.vaadin.flow.component.UI;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | STC Time")
public class DashboardView extends VerticalLayout {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final FlexTimeService flexTimeService;
    private final NotificationService notificationService;

    private Grid<Notification> notificationGrid;
    private Span unreadCountSpan;
    private Span flexBalanceSpan;

    public DashboardView(AuthService authService,
                         FlexTimeService flexTimeService,
                         NotificationService notificationService) {
        this.flexTimeService = flexTimeService;
        this.notificationService = notificationService;

        setSpacing(true);
        setPadding(true);

        User currentUser = authService.getCurrentUser();

        add(new H1("Welcome, " + (currentUser != null ? currentUser.getName() : "User") + "!"));
        add(createFlexTimeCard(currentUser));
        add(createNotificationsSection(currentUser));
    }

    private VerticalLayout createFlexTimeCard(User user) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle().set("background-color", "#f4f5f7");
        card.getStyle().set("padding", "15px");
        card.getStyle().set("border-radius", "8px");

        flexBalanceSpan = new Span();
        updateFlexBalance(user);

        card.add(new H3("Flex time account"), flexBalanceSpan);
        return card;
    }

    private void updateFlexBalance(User user) {
        if (user == null) {
            flexBalanceSpan.setText("N/A");
            return;
        }
        int balanceMinutes = flexTimeService.getBalanceMinutes(user);
        flexBalanceSpan.setText(TimeFormat.signed(balanceMinutes));
    }

    private VerticalLayout createNotificationsSection(User user) {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);

        unreadCountSpan = new Span();
        updateUnreadCount(user);

        Button markAllRead = new Button("Mark all as read", e -> {
            if (user != null) {
                notificationService.markAllRead(user);
                refreshData(user);
            }
        });

        header.add(new H3("Notifications"), unreadCountSpan, markAllRead);

        // Grid to display notifications
        notificationGrid = new Grid<>(Notification.class, false);
        notificationGrid.addComponentColumn(n -> {
            Span message = new Span(n.getMessage());
            message.getStyle().set("white-space", "normal");
            if (n.getLink() != null) {
                message.getStyle().set("cursor", "pointer");
            }
            return message;
        }).setHeader("Message").setFlexGrow(3);
        notificationGrid.addColumn(n -> n.getCreatedAt().format(
                java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).setHeader("Time");
        notificationGrid.addColumn(n -> n.isRead() ? "Read" : "Unread").setHeader("Status");

        notificationGrid.addItemClickListener(e -> {
            String link = e.getItem().getLink();
            if (link != null && !link.isBlank()) {
                UI.getCurrent().navigate(link);
            }
        });
        
        refreshGrid(user);

        section.add(header, notificationGrid);
        return section;
    }

    private void updateUnreadCount(User user) {
        if (user != null) {
            unreadCountSpan.setText("(" + notificationService.unreadCount(user) + " unread)");
        }
    }

    private void refreshGrid(User user) {
        if (user != null) {
            notificationGrid.setItems(notificationService.listFor(user));
        }
    }

    private void refreshData(User user) {
        updateFlexBalance(user);
        updateUnreadCount(user);
        refreshGrid(user);
    }
}