package com.example.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;

@Entity
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private TimeSheet timeSheet;

    private LocalDate date;
    private LocalTime beginTime;
    private LocalTime endTime;
    private int breakMinutes;

    @ManyToOne
    private Project project;     // optional

    private String comment;

    /** Worked minutes = (end - begin) - break. Not stored in the DB. */
    public int getWorkedMinutes() {
        if (beginTime == null || endTime == null) return 0;
        long total = Duration.between(beginTime, endTime).toMinutes();
        return (int) Math.max(0, total - breakMinutes);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TimeSheet getTimeSheet() {
		return timeSheet;
	}

	public void setTimeSheet(TimeSheet timeSheet) {
		this.timeSheet = timeSheet;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(LocalTime beginTime) {
		this.beginTime = beginTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public int getBreakMinutes() {
		return breakMinutes;
	}

	public void setBreakMinutes(int breakMinutes) {
		this.breakMinutes = breakMinutes;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

    // getters and setters: Source -> Generate Getters and Setters
}