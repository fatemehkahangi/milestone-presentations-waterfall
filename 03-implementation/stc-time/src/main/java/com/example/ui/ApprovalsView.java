package com.example.ui;

import com.example.domain.Role;
import com.example.security.AllowedRoles;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "approvals", layout = MainLayout.class)
@PageTitle("Approvals | STC Time")
@AllowedRoles(Role.SUPERVISOR)
public class ApprovalsView extends VerticalLayout {
    public ApprovalsView() {
        add(new H2("Approvals (placeholder)"));
    }
}