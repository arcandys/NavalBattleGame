package com.example.battleships_server.repository;

import java.util.List;
import java.util.Optional;

import com.example.battleships_server.Entity.Score;
import com.example.battleships_server.Entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Integer> {

    Optional<Score> findById(int id);
}
