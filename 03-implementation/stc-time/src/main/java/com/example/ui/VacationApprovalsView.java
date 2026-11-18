package com.example.ui;

import java.time.format.DateTimeFormatter;
import com.example.domain.Role;
import com.example.domain.User;
import com.example.domain.VacationRequest;
import com.example.security.AllowedRoles;
import com.example.security.AuthService;
import com.example.service.VacationService;
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

@Route(value = "vacation-approvals", layout = MainLayout.class)
@PageTitle("Vacation requests | STC Time")
@AllowedRoles(Role.SUPERVISOR)
public class VacationApprovalsView extends VerticalLayout {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final VacationService vacation;
    private final User user;
    private final Grid<VacationRequest> grid = new Grid<>();
    private final Span emptyLabel = new Span("No vacation requests to review");

    public VacationApprovalsView(AuthService auth, VacationService vacation) {
        this.vacation = vacation;
        this.user = auth.getCurrentUser();

        grid.addColumn(r -> r.getEmployee().getName()).setHeader("Employee");
        grid.addColumn(r -> r.getStartDate().format(DATE) + " - " + r.getEndDate().format(DATE))
                .setHeader("Period");
        grid.addColumn(VacationRequest::getWorkingDays).setHeader("Working days");
        grid.addComponentColumn(r -> {
            Button approve = new Button("Approve", e -> approve(r));
            approve.addThemeName("primary");
            Button reject = new Button("Reject", e -> openRejectDialog(r));
            reject.addThemeName("error");
            return new HorizontalLayout(approve, reject);
        });
        grid.setWidthFull();

        setWidthFull();
        add(new H2("Vacation requests"), emptyLabel, grid);
        refresh();
    }

    private void approve(VacationRequest r) {
        try {
            vacation.approve(r.getId());
            Notification.show("Vacation approved");
        } catch (IllegalArgumentException | IllegalStateException e) {
            Notification.show(e.getMessage());
        }
        refresh();
    }

    private void openRejectDialog(VacationRequest r) {
        Dialog dialog = new Dialog();
        dialog.add(new H3("Reject vacation of " + r.getEmployee().getName()));
        TextArea reason = new TextArea("Reason");
        reason.setWidthFull();
        dialog.add(reason);

        Button confirm = new Button("Reject", e -> {
            try {
                vacation.reject(r.getId(), reason.getValue());
                Notification.show("Vacation rejected");
                dialog.close();
                refresh();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                Notification.show(ex.getMessage());
            }
        });
        confirm.addThemeName("error");
        Button cancel = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancel, confirm);
        dialog.open();
    }

    private void refresh() {
        var pending = vacation.findPendingForSupervisor(user);
        grid.setItems(pending);
        emptyLabel.setVisible(pending.isEmpty());
        grid.setVisible(!pending.isEmpty());
    }
}