package com.example.battleships_server.repository;

import java.util.List;
import java.util.Optional;

import com.example.battleships_server.Entity.Ship;
import com.example.battleships_server.Entity.shipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Integer> {

    Optional<Ship> findById(int id);
}
