package com.example.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.example.domain.Absence;
import com.example.domain.Role;
import com.example.domain.User;
import com.example.security.AuthService;
import com.example.service.AbsenceService;
import com.example.service.VacationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "sickness", layout = MainLayout.class)
@PageTitle("Sickness | STC Time")
public class SicknessView extends VerticalLayout {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final AbsenceService absenceService;
    private final User user;
    private final boolean isHr;

    private final ComboBox<User> employeeField = new ComboBox<>("Employee");
    private final DatePicker startField = new DatePicker("First sick day");
    private final DatePicker endField = new DatePicker("Last sick day");
    private final Grid<Absence> grid = new Grid<>();

    public SicknessView(AuthService auth, AbsenceService absenceService) {
        this.absenceService = absenceService;
        this.user = auth.getCurrentUser();
        this.isHr = user.getRole() == Role.HR;

        setWidthFull();
        add(new H2("Sickness"));

        if (isHr) {
            employeeField.setItems(absenceService.listEmployees());
            employeeField.setItemLabelGenerator(User::getName);
            startField.setValue(LocalDate.now());
            endField.setValue(LocalDate.now());

            Button register = new Button("Register sickness", e -> register());
            register.addThemeName("primary");
            HorizontalLayout form = new HorizontalLayout(employeeField, startField, endField, register);
            form.setAlignItems(FlexComponent.Alignment.BASELINE);
            add(form);
        } else {
            add(new Paragraph("Please report sickness directly to HR (phone or email). "
                    + "HR registers it here and your supervisor is informed automatically."));
        }

        if (isHr) {
            grid.addColumn(a -> a.getEmployee().getName()).setHeader("Employee");
        }
        grid.addColumn(a -> a.getStartDate().format(DATE) + " - " + a.getEndDate().format(DATE))
                .setHeader("Period");
        grid.addColumn(a -> VacationService.countWorkingDays(a.getStartDate(), a.getEndDate()))
                .setHeader("Working days");
        grid.setWidthFull();
        add(grid);
        refresh();
    }

    private void register() {
        try {
            User employee = employeeField.getValue();
            absenceService.registerSickness(user, employee == null ? null : employee.getId(),
                    startField.getValue(), endField.getValue());
            Notification.show("Sickness registered, supervisor informed");
            refresh();
        } catch (IllegalArgumentException | IllegalStateException e) {
            Notification.show(e.getMessage());
        }
    }

    private void refresh() {
        grid.setItems(isHr ? absenceService.listAll() : absenceService.listFor(user));
    }
}