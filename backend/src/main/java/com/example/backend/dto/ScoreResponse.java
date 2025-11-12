package com.example.backend.dto;

import com.example.backend.model.Score;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * Response representation of a Score entity.
 */
public class ScoreResponse {

    @Schema(description = "Unique identifier of the score", example = "1")
    private Long id;

    @Schema(description = "Player name", example = "Alice")
    private String playerName;

    @Schema(description = "Points achieved", example = "250")
    private Integer points;

    @Schema(description = "Creation timestamp in UTC", example = "2025-01-01T00:00:00Z")
    private Instant createdAt;

    public ScoreResponse() {
    }

    public ScoreResponse(Long id, String playerName, Integer points, Instant createdAt) {
        this.id = id;
        this.playerName = playerName;
        this.points = points;
        this.createdAt = createdAt;
    }

    // PUBLIC_INTERFACE
    public static ScoreResponse fromEntity(Score entity) {
        /** Convert a Score entity to ScoreResponse. */
        return new ScoreResponse(entity.getId(), entity.getPlayerName(), entity.getPoints(), entity.getCreatedAt());
    }

    // PUBLIC_INTERFACE
    public Long getId() {
        /** Getter for id */
        return id;
    }

    // PUBLIC_INTERFACE
    public void setId(Long id) {
        /** Setter for id */
        this.id = id;
    }

    // PUBLIC_INTERFACE
    public String getPlayerName() {
        /** Getter for player name */
        return playerName;
    }

    // PUBLIC_INTERFACE
    public void setPlayerName(String playerName) {
        /** Setter for player name */
        this.playerName = playerName;
    }

    // PUBLIC_INTERFACE
    public Integer getPoints() {
        /** Getter for points */
        return points;
    }

    // PUBLIC_INTERFACE
    public void setPoints(Integer points) {
        /** Setter for points */
        this.points = points;
    }

    // PUBLIC_INTERFACE
    public Instant getCreatedAt() {
        /** Getter for createdAt */
        return createdAt;
    }

    // PUBLIC_INTERFACE
    public void setCreatedAt(Instant createdAt) {
        /** Setter for createdAt */
        this.createdAt = createdAt;
    }
}
