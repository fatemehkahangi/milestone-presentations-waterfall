package com.example.ui;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import com.example.domain.*;
import com.example.security.AuthService;
import com.example.service.*;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "timesheet", layout = MainLayout.class)
@PageTitle("Time sheet | STC Time")
public class TimeSheetView extends VerticalLayout {

    private static final DateTimeFormatter DAY_FORMAT =
            DateTimeFormatter.ofPattern("EEE dd.MM.", Locale.ENGLISH);

    private final WorkTimeService workTimeService;
    private final ProjectService projectService;
    private final TimeSheetService timeSheetService;
    private final User user;

    private YearMonth currentMonth = YearMonth.now();
    private TimeSheet sheet;

    // Header
    private final Span monthLabel = new Span();
    private final Span statusLabel = new Span();
    private final Span rejectionLabel = new Span();

    // Form
    private final DatePicker dateField = new DatePicker("Date");
    private final TimePicker beginField = new TimePicker("Begin");
    private final TimePicker endField = new TimePicker("End");
    private final IntegerField breakField = new IntegerField("Break (min)");
    private final ComboBox<Project> projectField = new ComboBox<>("Project");
    private final TextField commentField = new TextField("Comment");
    private final Button addButton = new Button("Add entry");
    private final Button newProjectButton = new Button("New project");

    // Table and footer
    private final Grid<TimeEntry> grid = new Grid<>();
    private final Span totalLabel = new Span();
    private final Button submitButton = new Button("Submit time sheet");

    public TimeSheetView(WorkTimeService workTimeService,
                         ProjectService projectService,
                         TimeSheetService timeSheetService,
                         AuthService auth) {

        this.workTimeService = workTimeService;
        this.projectService = projectService;
        this.timeSheetService = timeSheetService;
        this.user = auth.getCurrentUser();

        setWidthFull();

        add(new H2("Time sheet"));
        add(createMonthHeader());
        add(createForm());
        add(createGrid());
        add(createFooter());

        refresh();
    }

    // ---------------------------------------------------------------- layout

    private HorizontalLayout createMonthHeader() {
        Button previous = new Button("<", e -> {
            currentMonth = currentMonth.minusMonths(1);
            refresh();
        });
        Button next = new Button(">", e -> {
            currentMonth = currentMonth.plusMonths(1);
            refresh();
        });

        monthLabel.getStyle().set("font-weight", "bold").set("min-width", "9em")
                .set("text-align", "center");
        rejectionLabel.getStyle().set("color", "var(--lumo-error-text-color, #c00)");

        HorizontalLayout header = new HorizontalLayout(
                previous, monthLabel, next, statusLabel, rejectionLabel);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        return header;
    }

    private HorizontalLayout createForm() {
        breakField.setMin(0);
        breakField.setValue(0);
        breakField.setStepButtonsVisible(true);
        breakField.setWidth("9em");

        projectField.setItemLabelGenerator(Project::getName);
        projectField.setClearButtonVisible(true);
        reloadProjects();

        addButton.addThemeName("primary");
        addButton.addClickListener(e -> addEntry());
        newProjectButton.addClickListener(e -> openNewProjectDialog());

        HorizontalLayout form = new HorizontalLayout(
                dateField, beginField, endField, breakField,
                projectField, commentField, addButton, newProjectButton);
        form.setAlignItems(FlexComponent.Alignment.BASELINE);
        form.setWrap(true);
        return form;
    }

    private Grid<TimeEntry> createGrid() {
        grid.addColumn(entry -> entry.getDate().format(DAY_FORMAT)).setHeader("Date");
        grid.addColumn(TimeEntry::getBeginTime).setHeader("Begin");
        grid.addColumn(TimeEntry::getEndTime).setHeader("End");
        grid.addColumn(entry -> entry.getBreakMinutes() + " min").setHeader("Break");
        grid.addColumn(entry -> TimeFormat.plain(entry.getWorkedMinutes())).setHeader("Worked");
        grid.addColumn(entry -> entry.getProject() == null ? "" : entry.getProject().getName())
                .setHeader("Project");
        grid.addColumn(TimeEntry::getComment).setHeader("Comment");
        grid.addComponentColumn(entry -> {
            Button delete = new Button("Delete", e -> deleteEntry(entry));
            delete.addThemeName("error");
            delete.addThemeName("tertiary");
            delete.setEnabled(!TimeSheetService.isLocked(sheet));
            return delete;
        });
        grid.setWidthFull();
        return grid;
    }

    private HorizontalLayout createFooter() {
        submitButton.addThemeName("primary");
        submitButton.addClickListener(e -> submitSheet());

        HorizontalLayout footer = new HorizontalLayout(totalLabel, submitButton);
        footer.setAlignItems(FlexComponent.Alignment.CENTER);
        return footer;
    }

    // --------------------------------------------------------------- actions

    private void addEntry() {
        try {
            int breakMinutes = breakField.getValue() == null ? 0 : breakField.getValue();
            workTimeService.addEntry(user, dateField.getValue(),
                    beginField.getValue(), endField.getValue(), breakMinutes,
                    projectField.getValue(), commentField.getValue());

            Notification.show("Entry added");
            beginField.clear();
            endField.clear();
            commentField.clear();
            refresh();
        } catch (IllegalArgumentException | IllegalStateException e) {
            Notification.show(e.getMessage());
        }
    }

    private void deleteEntry(TimeEntry entry) {
        try {
            workTimeService.deleteEntry(entry.getId());
            Notification.show("Entry deleted");
            refresh();
        } catch (IllegalArgumentException | IllegalStateException e) {
            Notification.show(e.getMessage());
        }
    }

    private void submitSheet() {
        try {
            timeSheetService.submit(sheet.getId());
            Notification.show("Time sheet submitted to your supervisor");
            refresh();
        } catch (RuntimeException e) {
            Notification.show(e.getMessage());
        }
    }

    private void openNewProjectDialog() {
        Dialog dialog = new Dialog();
        dialog.add(new H3("New project"));

        TextField nameField = new TextField("Name");
        TextField descriptionField = new TextField("Description (optional)");
        nameField.setWidthFull();
        descriptionField.setWidthFull();
        dialog.add(new VerticalLayout(nameField, descriptionField));

        Button save = new Button("Save", e -> {
            try {
                Project project = projectService.createProject(
                        nameField.getValue(), descriptionField.getValue());
                reloadProjects();
                projectField.setValue(project);
                Notification.show("Project created");
                dialog.close();
            } catch (IllegalArgumentException ex) {
                Notification.show(ex.getMessage());
            }
        });
        save.addThemeName("primary");
        Button cancel = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancel, save);
        dialog.open();
        nameField.focus();
    }

    // --------------------------------------------------------------- refresh

    /** Reloads the sheet of the current month and updates every part of the page. */
    private void refresh() {
        sheet = workTimeService.getOrCreateTimeSheet(user, currentMonth);
        boolean locked = TimeSheetService.isLocked(sheet);

        // Header
        monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                + " " + currentMonth.getYear());
        statusLabel.setText("Status: " + sheet.getStatus());
        if (sheet.getStatus() == TimeSheetStatus.REJECTED && sheet.getRejectionReason() != null) {
            rejectionLabel.setText("Rejected: " + sheet.getRejectionReason());
        } else {
            rejectionLabel.setText("");
        }

        // The date picker only allows days of the shown month
        LocalDate first = currentMonth.atDay(1);
        LocalDate last = currentMonth.atEndOfMonth();
        dateField.setMin(first);
        dateField.setMax(last);
        LocalDate today = LocalDate.now();
        if (dateField.getValue() == null || !YearMonth.from(dateField.getValue()).equals(currentMonth)) {
            dateField.setValue(YearMonth.from(today).equals(currentMonth) ? today : first);
        }

        // Table and total
        List<TimeEntry> entries = workTimeService.getEntries(sheet);
        grid.setItems(entries);
        int total = 0;
        for (TimeEntry entry : entries) {
            total += entry.getWorkedMinutes();
        }
        totalLabel.setText("Month total: " + TimeFormat.plain(total));

        // Locked sheets cannot be edited or submitted again
        dateField.setEnabled(!locked);
        beginField.setEnabled(!locked);
        endField.setEnabled(!locked);
        breakField.setEnabled(!locked);
        projectField.setEnabled(!locked);
        commentField.setEnabled(!locked);
        addButton.setEnabled(!locked);
        newProjectButton.setEnabled(!locked);
        submitButton.setEnabled(!locked && !entries.isEmpty());
    }

    private void reloadProjects() {
        projectField.setItems(projectService.listProjects());
    }
}
