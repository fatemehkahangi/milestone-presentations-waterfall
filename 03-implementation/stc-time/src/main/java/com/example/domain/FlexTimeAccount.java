package com.example.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class FlexTimeAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private User user;

    private int balanceMinutes = 0;

    private LocalDateTime lastUpdated = LocalDateTime.now();

    // getters and setters: Source -> Generate Getters and Setters
}