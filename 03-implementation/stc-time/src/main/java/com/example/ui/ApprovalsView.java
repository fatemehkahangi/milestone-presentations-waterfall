
package com.example.ui;

import java.util.List;

import com.example.domain.*;
import com.example.security.AllowedRoles;
import com.example.security.AuthService;
import com.example.service.*;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "approvals", layout = MainLayout.class)
@PageTitle("Approvals | STC Time")
@AllowedRoles(Role.SUPERVISOR)
public class ApprovalsView extends VerticalLayout {

    private final TimeSheetService timeSheetService;
    private final WorkTimeService workTimeService;
    private final FlexTimeService flexTimeService;
    private final AuthService auth;

    private final Grid<TimeSheet> grid = new Grid<>();
    private final Span emptyMessage = new Span("No time sheets to review");

    public ApprovalsView(
            TimeSheetService timeSheetService,
            WorkTimeService workTimeService,
            FlexTimeService flexTimeService,
            AuthService auth) {

        this.timeSheetService = timeSheetService;
        this.workTimeService = workTimeService;
        this.flexTimeService = flexTimeService;
        this.auth = auth;

        add(new H2("Approvals"));

        grid.addColumn(sheet -> sheet.getEmployee().getName())
                .setHeader("Employee");

        grid.addColumn(TimeSheet::getMonth)
                .setHeader("Month");

        grid.addColumn(TimeSheet::getYear)
                .setHeader("Year");

        grid.addComponentColumn(sheet -> {
            Button reviewButton = new Button("Review");

            reviewButton.addClickListener(event -> {
                openReviewDialog(sheet);
            });

            return reviewButton;
        }).setHeader("Action");

        grid.setWidthFull();

        add(grid, emptyMessage);

        refreshGrid();
    }

    // Refresh pending time sheets
    private void refreshGrid() {

        User supervisor = auth.getCurrentUser();

        List<TimeSheet> pending =
                timeSheetService.findPendingForSupervisor(supervisor);

        grid.setItems(pending);

        if (pending.isEmpty()) {
            emptyMessage.setVisible(true);
        } else {
            emptyMessage.setVisible(false);
        }
    }

    // Open review dialog
    private void openReviewDialog(TimeSheet sheet) {

        Dialog dialog = new Dialog();

        dialog.setWidth("800px");

        dialog.add(new H3(
                "Time sheet: " + sheet.getEmployee().getName()));

        dialog.add(new Span(
                "Month: " + sheet.getMonth()
                + "/" + sheet.getYear()));

        // Read-only entries
        Grid<TimeEntry> entriesGrid = new Grid<>();

        entriesGrid.addColumn(TimeEntry::getDate)
                .setHeader("Date");

        entriesGrid.addColumn(TimeEntry::getBeginTime)
                .setHeader("Begin");

        entriesGrid.addColumn(TimeEntry::getEndTime)
                .setHeader("End");

        entriesGrid.addColumn(TimeEntry::getBreakMinutes)
                .setHeader("Break (min)");

        entriesGrid.addColumn(TimeEntry::getWorkedMinutes)
                .setHeader("Worked (min)");

        entriesGrid.addColumn(TimeEntry::getComment)
                .setHeader("Comment");

        List<TimeEntry> entries =
                workTimeService.getEntries(sheet);

        entriesGrid.setItems(entries);
        entriesGrid.setWidthFull();

        dialog.add(entriesGrid);

        // Approve button
        Button approveButton = new Button("Approve");

        approveButton.addClickListener(event -> {

            try {
                timeSheetService.approve(sheet.getId());

                int balance = flexTimeService.getBalanceMinutes(
                        sheet.getEmployee());

                if (FlexTimeService.isOutOfRange(balance)) {
                    Notification.show(
                            "Warning: Flex time balance exceeds +/-100 hours");
                } else {
                    Notification.show("Time sheet approved");
                }

                dialog.close();
                refreshGrid();

            } catch (RuntimeException e) {
                Notification.show(e.getMessage());
            }
        });

        // Reject button
        Button rejectButton = new Button("Reject");

        TextArea reasonField = new TextArea("Rejection reason");
        reasonField.setWidthFull();

        reasonField.setVisible(false);

        Button confirmRejectButton = new Button("Confirm rejection");
        confirmRejectButton.setVisible(false);

        rejectButton.addClickListener(event -> {
            reasonField.setVisible(true);
            confirmRejectButton.setVisible(true);
        });

        confirmRejectButton.addClickListener(event -> {

            String reason = reasonField.getValue();

            if (reason == null || reason.trim().isEmpty()) {
                Notification.show("Rejection reason is required");
                return;
            }

            try {
                timeSheetService.reject(sheet.getId(), reason);

                Notification.show("Time sheet rejected");

                dialog.close();
                refreshGrid();

            } catch (RuntimeException e) {
                Notification.show(e.getMessage());
            }
        });

        Button closeButton = new Button("Close", event -> {
            dialog.close();
        });

        HorizontalLayout buttons = new HorizontalLayout(
                approveButton, rejectButton, closeButton);

        dialog.add(buttons);
        dialog.add(reasonField, confirmRejectButton);

        dialog.open();
    }
}
