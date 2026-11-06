package com.example.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | STC Time")
public class DashboardView extends VerticalLayout {
    public DashboardView() {
        add(new H2("Dashboard (placeholder)"));
    }
}