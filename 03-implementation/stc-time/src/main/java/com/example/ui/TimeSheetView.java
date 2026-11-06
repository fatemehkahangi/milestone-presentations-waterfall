package com.example.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "timesheet", layout = MainLayout.class)
@PageTitle("Time sheet | STC Time")
public class TimeSheetView extends VerticalLayout {
    public TimeSheetView() {
        add(new H2("Time sheet (placeholder)"));
    }
}