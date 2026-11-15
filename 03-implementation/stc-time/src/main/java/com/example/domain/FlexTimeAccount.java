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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getBalanceMinutes() {
		return balanceMinutes;
	}

	public void setBalanceMinutes(int balanceMinutes) {
		this.balanceMinutes = balanceMinutes;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

    // getters and setters: Source -> Generate Getters and Setters
}