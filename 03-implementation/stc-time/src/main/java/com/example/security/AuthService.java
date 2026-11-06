package com.example.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.domain.User;
import com.example.repository.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

@Service
public class AuthService {

    private static final String SESSION_KEY = "currentUser";

    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean login(String email, String password) {
        return users.findByEmail(email)
                .filter(User::isActive)
                .filter(u -> encoder.matches(password, u.getPasswordHash()))
                .map(u -> {
                    VaadinSession.getCurrent().setAttribute(SESSION_KEY, u);
                    return true;
                })
                .orElse(false);
    }

    /** Returns the logged-in user, or null if nobody is logged in. */
    public User getCurrentUser() {
        VaadinSession session = VaadinSession.getCurrent();
        return session == null ? null : (User) session.getAttribute(SESSION_KEY);
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation("login");
        VaadinSession.getCurrent().getSession().invalidate();
    }
}