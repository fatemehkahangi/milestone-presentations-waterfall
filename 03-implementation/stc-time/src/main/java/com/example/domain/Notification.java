package com.example.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User recipient;

    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_read")
    private boolean read = false;

    // getters and setters: Source -> Generate Getters and Setters
}