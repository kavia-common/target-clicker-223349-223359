package com.example.backend.controller;

import com.example.backend.dto.CreateScoreRequest;
import com.example.backend.model.Score;
import com.example.backend.repository.ScoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Score APIs using SpringBootTest + MockMvc.
 *
 * Covers:
 * 1) POST /api/scores persists and returns 201 with JSON matching ScoreResponse.
 * 2) GET /api/scores/top returns scores sorted descending and limited to 10 (repository impl).
 * 3) Validation rejects negative or excessively large scores (400 with error body).
 * 4) Ensure Content-Type is application/json.
 *
 * Uses H2 in-memory DB configured in application.properties. Each test runs in a transaction and rolls back.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class ScoreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clean() {
        // In case @Transactional scope changes, ensure tests are isolated.
        // With @Transactional on the class, this typically isn't needed, but it's harmless.
        scoreRepository.flush();
    }

    @Nested
    class CreateScoreTests {

        @Test
        @DisplayName("POST /api/scores with valid payload returns 201 and persists entity")
        void postScore_valid_createsAndPersists() throws Exception {
            CreateScoreRequest request = new CreateScoreRequest("Alice", 123);

            String json = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/scores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.playerName").value("Alice"))
                    .andExpect(jsonPath("$.points").value(123))
                    .andExpect(jsonPath("$.createdAt").exists());

            // Verify persistence
            List<Score> all = scoreRepository.findAll();
            assertThat(all).hasSize(1);
            Score saved = all.get(0);
            assertThat(saved.getPlayerName()).isEqualTo("Alice");
            assertThat(saved.getPoints()).isEqualTo(123);
            assertThat(saved.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("POST /api/scores rejects negative score with 400 and error body")
        void postScore_negative_rejected() throws Exception {
            CreateScoreRequest request = new CreateScoreRequest("Bob", -5);
            String json = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/scores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value(Matchers.containsStringIgnoringCase("bad request")))
                    .andExpect(jsonPath("$.detail").value(Matchers.containsStringIgnoringCase("validation failed")))
                    .andExpect(jsonPath("$.errors.points").exists());
        }

        @Test
        @DisplayName("POST /api/scores rejects excessively large score with 400 and error body")
        void postScore_tooLarge_rejected() throws Exception {
            CreateScoreRequest request = new CreateScoreRequest("Carol", 1_000_000_001);
            String json = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/scores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.errors.points").exists());
        }

        @Test
        @DisplayName("POST /api/scores rejects blank playerName with 400 and error body")
        void postScore_blankName_rejected() throws Exception {
            CreateScoreRequest request = new CreateScoreRequest("   ", 10);
            String json = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/scores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.errors.playerName").exists());
        }
    }

    @Nested
    class TopScoresTests {

        @Test
        @DisplayName("GET /api/scores/top returns up to 10 scores sorted by points desc then createdAt desc")
        void getTopScores_sortedAndLimited() throws Exception {
            // Seed 12 scores with varying points and createdAt ordering via JPA @PrePersist
            for (int i = 1; i <= 12; i++) {
                scoreRepository.save(new Score("Player" + i, i * 10));
            }
            scoreRepository.flush();

            mockMvc.perform(get("/api/scores/top")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", Matchers.hasSize(10)))
                    // Highest first: last inserted has highest points (12*10=120)
                    .andExpect(jsonPath("$[0].points").value(120))
                    .andExpect(jsonPath("$[0].playerName").value("Player12"))
                    .andExpect(jsonPath("$[9].points").value(30))  // tenth item should be 30 (players 12..3)
                    .andExpect(jsonPath("$[9].playerName").value("Player3"));
        }
    }
}
