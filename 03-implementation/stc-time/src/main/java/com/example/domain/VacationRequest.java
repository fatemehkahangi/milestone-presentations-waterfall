package com.example.domain;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
public class VacationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User employee;

    private LocalDate startDate;
    private LocalDate endDate;

    /** Working days (Mon-Fri) between start and end, inclusive. */
    private int workingDays;

    @Enumerated(EnumType.STRING)
    private VacationStatus status = VacationStatus.REQUESTED;

    private String rejectionReason;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(int workingDays) {
		this.workingDays = workingDays;
	}

	public VacationStatus getStatus() {
		return status;
	}

	public void setStatus(VacationStatus status) {
		this.status = status;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

    // getters and setters: Source -> Generate Getters and Setters
}