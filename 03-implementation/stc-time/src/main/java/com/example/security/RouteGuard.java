package com.example.security;

import java.util.Arrays;
import org.springframework.stereotype.Component;
import com.example.domain.User;
import com.example.ui.LoginView;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

@Component
public class RouteGuard implements VaadinServiceInitListener {

    private final AuthService auth;

    public RouteGuard(AuthService auth) {
        this.auth = auth;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent ->
                uiEvent.getUI().addBeforeEnterListener(this::check));
    }

    private void check(BeforeEnterEvent event) {
        Class<?> target = event.getNavigationTarget();
        if (LoginView.class.equals(target)) return;

        User user = auth.getCurrentUser();
        if (user == null) {
            event.rerouteTo(LoginView.class);
            return;
        }

        AllowedRoles allowed = target.getAnnotation(AllowedRoles.class);
        if (allowed != null && !Arrays.asList(allowed.value()).contains(user.getRole())) {
            event.rerouteTo(""); // back to the dashboard
        }
    }
}