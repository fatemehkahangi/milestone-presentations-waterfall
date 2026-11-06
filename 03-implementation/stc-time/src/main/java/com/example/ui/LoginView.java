package com.example.ui;

import com.example.security.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | STC Time")
public class LoginView extends VerticalLayout {

    public LoginView(AuthService auth) {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        LoginForm form = new LoginForm();
        form.addLoginListener(e -> {
            if (auth.login(e.getUsername(), e.getPassword())) {
                UI.getCurrent().navigate("");
            } else {
                form.setError(true);
            }
        });
        add(new H1("STC Time"), form);
    }
}