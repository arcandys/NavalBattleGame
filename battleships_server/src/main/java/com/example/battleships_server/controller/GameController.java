package com.example.battleships_server.controller;

import com.example.battleships_server.Entity.FireResultDTO;
import com.example.battleships_server.Entity.*;
import com.example.battleships_server.service.gameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
public class GameController {
    private final gameService gameService;
    private grid gameGrid;
    private boolean isGameStarted = false;

    public GameController(gameService gameService) {
        this.gameService = gameService;
        this.gameGrid = new grid(10, 10);
    }

    private void initializeGame() {
        gameService.createGrid(10, 10);

        // add ship to the grid for tests
        /*Ship ship_1 = new Ship(shipType.AIRCRAFT_CARRIER, direction.HORIZONTAL_RIGHT);
        Tile tile_ship_1 = new Tile(3, 3);
        Ship ship_2 = new Ship(shipType.AIRCRAFT_CARRIER, direction.VERTICAL_BOTTOM);
        Tile tile_ship_2 = new Tile(4, 2);
        Ship ship_3 = new Ship(shipType.AIRCRAFT_CARRIER, direction.VERTICAL_BOTTOM);
        Tile tile_ship_3 = new Tile(4, 2);*/

        /*gameService.placeShip(gameService.createShip(ship_1), tile_ship_1, this.gameGrid);
        gameService.placeShip(gameService.createShip(ship_2), tile_ship_2, this.gameGrid);
        gameService.placeShip(gameService.createShip(ship_3), tile_ship_3, this.gameGrid);

        FOR TESTING PURPOSES ONLY - NOT USED ANYMORE*/
        Random random = new Random();
        gameService.placeRandomShips(this.gameGrid, random);
        printGrid(this.gameGrid);
    }

    @PostMapping("/game/start")
    public ResponseEntity<char[][]> startGame() {
        if(!isGameStarted){
            initializeGame();
            isGameStarted = true;
        }
        char[][] grid = new char[10][10];
        return ResponseEntity.ok().body(grid);
    }

    @PostMapping("/game/fire")
    public ResponseEntity<String> fire(@RequestParam int x, @RequestParam int y) {
        if (this.gameGrid == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game grid not created.");
        }
        FireResultDTO fireResultDTO = gameService.fireTile(x, y, this.gameGrid);

        boolean isTileHit = fireResultDTO.isTileHit();
        boolean isShipSunk = fireResultDTO.isShipSunk();
        boolean isGameFinished = fireResultDTO.isGameFinished();

        return ResponseEntity.ok("isTileHit: " + isTileHit + ", isShipSunk: " + isShipSunk + ", isGameFinished: " + isGameFinished);
    }

    @PostMapping("/game/stop")
    public ResponseEntity<String> stopGame(@RequestParam String teamName, @RequestParam String gameId){
        gameService.resetGame(this.gameGrid);
        isGameStarted = false;
        return ResponseEntity.ok("teamName: " + teamName + ", gameId: " + gameId);
    }

    private void printGrid(grid game_grid) {
        for (int i = 0; i < game_grid.getLength(); i++) {
            for (int j = 0; j < game_grid.getWidth(); j++) {
                System.out.print(game_grid.getTile(i, j).getX() + ";" + game_grid.getTile(i, j).getY() + "  " +game_grid.getTile(i, j).getShipId() + game_grid.getTile(i, j).getShipTileStatus() + ";" + game_grid.getTile(i, j).isOccupied() +"   // ");
            }
            System.out.println();
        }
    }
}