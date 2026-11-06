package com.example.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "year", "month"}))
public class TimeSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User employee;

    @Column(name = "year")
    private int year;

    @Column(name = "month")
    private int month;           // 1 to 12

    @Enumerated(EnumType.STRING)
    private TimeSheetStatus status = TimeSheetStatus.OPEN;

    private LocalDateTime submittedAt;

    private String rejectionReason;

    @OneToMany(mappedBy = "timeSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeEntry> entries = new ArrayList<>();

    // getters and setters: Source -> Generate Getters and Setters
}