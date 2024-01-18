package com.example.battleships_server;

import com.example.battleships_server.Entity.*;
import com.example.battleships_server.service.gameService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class dataLoad implements CommandLineRunner {
    private final gameService gameService;

    public dataLoad(gameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void run(String...args) throws Exception {
        /*Ship ship_1 = new Ship(shipType.AIRCRAFT_CARRIER, direction.HORIZONTAL_RIGHT);
        Tile tile_ship_1 = new Tile(3, 3);
        grid game_grid = new grid(10, 10);
        Ship ship_2 = new Ship(shipType.AIRCRAFT_CARRIER, direction.VERTICAL_BOTTOM);
        Tile tile_ship_2 = new Tile(4, 2);
        Ship ship_3 = new Ship(shipType.AIRCRAFT_CARRIER, direction.VERTICAL_BOTTOM);
        Tile tile_ship_3 = new Tile(4, 2);

        gameService.createGrid(10, 10);
        gameService.placeShip(gameService.createShip(ship_1), tile_ship_1, game_grid);
        gameService.placeShip(gameService.createShip(ship_2), tile_ship_2, game_grid);
        gameService.placeShip(gameService.createShip(ship_3), tile_ship_3, game_grid);

        printGrid(game_grid);


        gameService.fireTile(3,3,game_grid);
        gameService.fireTile(3,4,game_grid);
        gameService.fireTile(3,5,game_grid);
        gameService.fireTile(3,6,game_grid);
        gameService.fireTile(4,2,game_grid);
        gameService.fireTile(5,2,game_grid);
        gameService.fireTile(6,2,game_grid);
        gameService.fireTile(7,2,game_grid);
    }

    private void printGrid(grid game_grid) {
        for (int i = 0; i < game_grid.getLength(); i++) {
            for (int j = 0; j < game_grid.getWidth(); j++) {
                System.out.print(game_grid.getTile(i, j).getX() + ";" + game_grid.getTile(i, j).getY() + " " +game_grid.getTile(i, j).getShipId() + game_grid.getTile(i, j).getShipTileStatus() + ";" + game_grid.getTile(i, j).isOccupied() +" // ");
            }
            System.out.println();
        }

        WAS FOR TESTING ONLY - NOT USED ANYMORE
    */}
}
