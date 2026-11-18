package com.example.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.example.domain.User;
import com.example.domain.VacationRequest;
import com.example.domain.VacationStatus;
import com.example.security.AuthService;
import com.example.service.VacationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "vacation", layout = MainLayout.class)
@PageTitle("Vacation | STC Time")
public class VacationView extends VerticalLayout {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final VacationService vacation;
    private final User user;

    private final DatePicker startField = new DatePicker("Start date");
    private final DatePicker endField = new DatePicker("End date");
    private final Span daysLabel = new Span("Working days: 0");
    private final Span summaryLabel = new Span();
    private final Grid<VacationRequest> grid = new Grid<>();

    public VacationView(AuthService auth, VacationService vacation) {
        this.vacation = vacation;
        this.user = auth.getCurrentUser();

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        startField.setMin(tomorrow);
        endField.setMin(tomorrow);
        startField.addValueChangeListener(e -> updateDays());
        endField.addValueChangeListener(e -> updateDays());

        Button request = new Button("Request vacation", e -> requestVacation());
        request.addThemeName("primary");

        HorizontalLayout form = new HorizontalLayout(startField, endField, daysLabel, request);
        form.setAlignItems(FlexComponent.Alignment.BASELINE);

        grid.addColumn(r -> r.getStartDate().format(DATE) + " - " + r.getEndDate().format(DATE))
                .setHeader("Period");
        grid.addColumn(VacationRequest::getWorkingDays).setHeader("Working days");
        grid.addColumn(VacationRequest::getStatus).setHeader("Status");
        grid.addComponentColumn(r -> {
            Span reason = new Span(r.getRejectionReason() == null ? "" : r.getRejectionReason());
            reason.getStyle().set("white-space", "normal");   // wrap instead of cutting off
            return reason;
        }).setHeader("Reason").setFlexGrow(3).setWidth("20em");
        grid.addComponentColumn(r -> {
            Button delete = new Button("Delete", e -> cancel(r));
            delete.addThemeName("error");
            delete.addThemeName("tertiary");
            boolean cancelable = (r.getStatus() == VacationStatus.REQUESTED
                    || r.getStatus() == VacationStatus.APPROVED)
                    && r.getStartDate().isAfter(LocalDate.now());
            delete.setEnabled(cancelable);
            return delete;
        });
        grid.setWidthFull();

        setWidthFull();
        add(new H2("Vacation"), summaryLabel, form, grid);
        refresh();
    }

    private void updateDays() {
        daysLabel.setText("Working days: "
                + VacationService.countWorkingDays(startField.getValue(), endField.getValue()));
    }

    private void requestVacation() {
        try {
            vacation.request(user, startField.getValue(), endField.getValue());
            Notification.show("Vacation requested");
            startField.clear();
            endField.clear();
            refresh();
        } catch (IllegalArgumentException | IllegalStateException e) {
            Notification.show(e.getMessage());
        }
    }

    private void cancel(VacationRequest r) {
        try {
            vacation.cancel(r.getId());
            Notification.show("Vacation deleted");
            refresh();
        } catch (IllegalArgumentException | IllegalStateException e) {
            Notification.show(e.getMessage());
        }
    }

    private void refresh() {
        VacationService.Summary s = vacation.summary(user, LocalDate.now().getYear());
        summaryLabel.setText("This year: taken " + s.taken() + " | planned " + s.planned()
                + " | pending " + s.pending() + " | open " + s.open());
        grid.setItems(vacation.listFor(user));
    }
}