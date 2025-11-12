package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * PUBLIC_INTERFACE
 * Data Transfer Object for creating a new Score.
 */
public class CreateScoreRequest {

    @NotBlank(message = "playerName is required")
    @Size(min = 1, max = 100, message = "playerName length must be between 1 and 100")
    @Schema(description = "Name of the player", example = "Alice", maxLength = 100)
    private String playerName;

    @Min(value = 0, message = "points must be >= 0")
    @Max(value = 1000000000, message = "points must be realistic")
    @Schema(description = "Points achieved by the player", example = "250", minimum = "0")
    private Integer points;

    public CreateScoreRequest() {
    }

    public CreateScoreRequest(String playerName, Integer points) {
        this.playerName = playerName;
        this.points = points;
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
}
