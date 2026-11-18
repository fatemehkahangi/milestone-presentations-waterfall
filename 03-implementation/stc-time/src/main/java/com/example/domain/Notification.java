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

    private String link;   // route to open when the notification is clicked, e.g. "vacation"

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_read")
    private boolean read = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

    // getters and setters: Source -> Generate Getters and Setters
}