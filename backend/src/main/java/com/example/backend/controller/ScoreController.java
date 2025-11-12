package com.example.backend.controller;

import com.example.backend.dto.CreateScoreRequest;
import com.example.backend.dto.ScoreResponse;
import com.example.backend.model.Score;
import com.example.backend.repository.ScoreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * PUBLIC_INTERFACE
 * REST controller exposing APIs to create and retrieve Click Quest scores.
 *
 * Endpoints:
 * - POST /api/scores: Create a new score entry
 * - GET  /api/scores/top: Retrieve the top 10 scores ordered by points desc, createdAt desc
 */
@RestController
@RequestMapping(path = "/api/scores", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(name = "Scores", description = "Endpoints for creating and retrieving game scores")
public class ScoreController {

    private final ScoreRepository scoreRepository;

    public ScoreController(ScoreRepository scoreRepository) {
        this.scoreRepository = Objects.requireNonNull(scoreRepository, "scoreRepository");
    }

    /**
     * PUBLIC_INTERFACE
     * Create a new score entry.
     *
     * Request body:
     * {
     *   "playerName": "Alice",
     *   "points": 123
     * }
     *
     * Returns the created score object with id and createdAt.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new score",
            description = "Stores a player's score. Validates inputs and returns the created score with identifiers.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Score created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ScoreResponse.class),
                                    examples = @ExampleObject(value = "{\"id\":1,\"playerName\":\"Alice\",\"points\":100,\"createdAt\":\"2025-01-01T00:00:00Z\"}")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            }
    )
    public ScoreResponse createScore(@Valid @RequestBody CreateScoreRequest request) {
        // Basic sanitization (trim) in addition to validation annotations
        String sanitizedName = request.getPlayerName().trim();
        Score entity = new Score(sanitizedName, request.getPoints());
        Score saved = scoreRepository.save(entity);
        return ScoreResponse.fromEntity(saved);
    }

    /**
     * PUBLIC_INTERFACE
     * Retrieve the top 10 scores ordered by points desc and createdAt desc.
     */
    @GetMapping(path = "/top")
    @Operation(
            summary = "Get top scores",
            description = "Returns the top 10 scores ordered by points (desc) and createdAt (desc).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Top scores returned",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public List<ScoreResponse> getTopScores() {
        return scoreRepository.findTop10ByOrderByPointsDescCreatedAtDesc()
                .stream()
                .map(ScoreResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
