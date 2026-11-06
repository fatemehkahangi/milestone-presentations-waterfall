package com.example.ui;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.security.AuthService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

public class MainLayout extends AppLayout {

    public MainLayout(AuthService auth) {
        User user = auth.getCurrentUser();

        Span title = new Span("STC Time");
        Span who = new Span(user.getName() + " (" + user.getRole() + ")");
        Button logout = new Button("Logout", e -> auth.logout());
        addToNavbar(new DrawerToggle(), title, who, logout);

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", ""));
        nav.addItem(new SideNavItem("Time sheet", "timesheet"));
        if (user.getRole() == Role.SUPERVISOR) {
            nav.addItem(new SideNavItem("Approvals", "approvals"));
        }
        addToDrawer(nav);
    }
}