package com.example.backend.repository;

import com.example.backend.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Repository for accessing Score entities.
 *
 * Provides CRUD operations and custom finder methods.
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    /**
     * PUBLIC_INTERFACE
     * Find all scores ordered by points descending, then by createdAt descending.
     *
     * @return List of scores sorted by highest points first.
     */
    List<Score> findAllByOrderByPointsDescCreatedAtDesc();

    /**
     * PUBLIC_INTERFACE
     * Find top N scores ordered by points descending.
     *
     * Note: Spring Data will automatically apply limit at runtime when used with Pageable;
     * this method name is for clarity and typical usage without pageable isn't limited.
     */
    List<Score> findTop10ByOrderByPointsDescCreatedAtDesc();
}
