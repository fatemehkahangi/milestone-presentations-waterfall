package com.example.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.domain.*;
import com.example.repository.UserRepository;
import com.example.security.AuthService;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository users;
    private final AuthService auth;

    public DataInitializer(UserRepository users, AuthService auth) {
        this.users = users;
        this.auth = auth;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) return;

        User ingrid = create("Ingrid Halvorsen", "ingrid@stc.de", Role.SUPERVISOR, null);
        create("Malte Jansen", "malte@stc.de", Role.EMPLOYEE, ingrid);
        create("Alex Becker", "alex@stc.de", Role.EMPLOYEE, ingrid);
        create("Sarah Schmidt", "sarah@stc.de", Role.HR, null);
        create("Admin", "admin@stc.de", Role.ADMIN, null);
    }

    private User create(String name, String email, Role role, User supervisor) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setRole(role);
        u.setSupervisor(supervisor);
        u.setPasswordHash(auth.hash("test123"));
        return users.save(u);
    }
}