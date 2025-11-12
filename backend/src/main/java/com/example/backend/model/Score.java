package com.example.backend.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * Represents a player's score entry for the Click Quest game.
 *
 * This entity is stored in an H2 database (in-memory by default).
 * Fields:
 * - id: Primary key, auto-generated.
 * - playerName: Name or identifier of the player.
 * - points: Total points achieved.
 * - createdAt: Timestamp when this score was recorded.
 */
@Entity
@Table(name = "scores")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_name", nullable = false, length = 100)
    private String playerName;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Score() {
        // Default constructor required by JPA
    }

    public Score(String playerName, Integer points) {
        this.playerName = playerName;
        this.points = points;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    // No public setter for id; it's auto-generated
    public String getPlayerName() {
        return playerName;
    }

    // PUBLIC_INTERFACE
    public void setPlayerName(String playerName) {
        /**
         * Set the player's name for this score entry.
         * @param playerName Non-null, up to 100 characters.
         */
        this.playerName = playerName;
    }

    public Integer getPoints() {
        return points;
    }

    // PUBLIC_INTERFACE
    public void setPoints(Integer points) {
        /**
         * Set the points earned for this score entry.
         * @param points Non-null integer points.
         */
        this.points = points;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // No setter for createdAt; managed by lifecycle
}
