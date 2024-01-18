package com.example.battleships_server.service;

import com.example.battleships_server.Entity.*;
import com.example.battleships_server.repository.ScoreRepository;
import com.example.battleships_server.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class gameService {
    @Autowired
    private ShipRepository shipRepository;
    private ScoreRepository scoreRepository;

    private FireResultDTO fireResultDTO;


    public List<Ship> getAllShips() {
        return shipRepository.findAll(); //return the list of all the ship in repository
    }

    public void deleteShip(int shipId) {
        shipRepository.deleteById(shipId); //delete the ship in repository with the id
    }

    public grid createGrid(int width, int height) {
        grid grid = new grid(width, height);
        return grid;
    }

    public void placeRandomShips(grid gameGrid, Random random) {

        Tile[] shipTiles_1 = new Tile[4];
        Ship[] cruiserShip = new Ship[2];
        Ship[] destroyerShip = new Ship[3];
        Ship[] torpedoBoatShips = new Ship[4];
        boolean isShipPlaced = false;

        // Place AIRCRAFT_CARRIER

        direction ship_Direction = getRandomDirection(random);
        Ship aircraftCarrier = new Ship(shipType.AIRCRAFT_CARRIER, ship_Direction);
        do{
            int x = random.nextInt(gameGrid.getLength());
            int y = random.nextInt(gameGrid.getWidth());
            Tile ship_1_tile = new Tile(x, y);
            isShipPlaced = this.placeShip(this.createShip(aircraftCarrier), ship_1_tile, gameGrid);
        } while(!isShipPlaced);


        // Place CRUISER ships
        for (int i = 0; i < 2; i++) {
            ship_Direction = getRandomDirection(random);
            cruiserShip[i] = new Ship(shipType.CRUISER, ship_Direction);
            isShipPlaced = false;
            do{
                int x = random.nextInt(gameGrid.getLength());
                int y = random.nextInt(gameGrid.getWidth());
                shipTiles_1[i] = new Tile(x, y);
                isShipPlaced = this.placeShip(this.createShip(cruiserShip[i]), shipTiles_1[i], gameGrid);
            } while(!isShipPlaced);
        }

        // Place DESTROYER ships
        for (int i = 0; i < 3; i++) {
            ship_Direction = getRandomDirection(random);
            destroyerShip[i] = new Ship(shipType.DESTROYER, ship_Direction);
            isShipPlaced = false;
            do {
                int x = random.nextInt(gameGrid.getLength());
                int y = random.nextInt(gameGrid.getWidth());
                shipTiles_1[i] = new Tile(x, y);
                isShipPlaced = this.placeShip(this.createShip(destroyerShip[i]), shipTiles_1[i], gameGrid);
            } while(!isShipPlaced);
        }

        // Place TORPEDO_BOAT ships
        for (int i = 0; i < 4; i++) {
            isShipPlaced = false;
            ship_Direction = getRandomDirection(random);
            torpedoBoatShips[i] = new Ship(shipType.TORPEDO_BOAT, ship_Direction);
            do {
                int x = random.nextInt(gameGrid.getLength());
                int y = random.nextInt(gameGrid.getWidth());
                shipTiles_1[i] = new Tile(x, y);
                isShipPlaced = this.placeShip(this.createShip(torpedoBoatShips[i]), shipTiles_1[i], gameGrid);
            } while(!isShipPlaced);
        }
    }


    public Ship createShip(Ship ship) {
        Ship newship = new Ship(ship.getShipType(), ship.getDirection());
        shipRepository.save(newship);
        System.out.print("newship: " + newship.getShipType() + " " + newship.getId() + "\n");
        return newship;
    }

    public boolean placeShip(Ship ship, Tile tile, grid grid) {
        boolean isShipPlaced;
        isShipPlaced = grid.placeShip(ship, tile, grid);
        shipRepository.save(ship);
        return isShipPlaced;
    }

    public Ship getShipById(int shipId) {
        Optional<Ship> optionalShip = shipRepository.findById(shipId);
        return optionalShip.orElse(null);
    }

    public FireResultDTO fireTile(int x, int y, grid gameGrid) {
        fireResultDTO = new FireResultDTO();
        Tile targetTile = gameGrid.getTile(x, y);
        System.out.println("Selected tile: " + targetTile.isOccupied());

        if (targetTile != null && targetTile.isOccupied()) {
            int shipId = targetTile.getShipId();
            Ship targetedShip = getShipById(shipId);

            if (targetedShip != null) {
                if (targetedShip.getDamageCount() == targetedShip.getShipType().getSize()) {
                    System.out.println("Ship sunk!");
                }
                if (targetTile.getShipTileStatus()) {
                    targetTile.setShipTileStatus(false);
                    fireResultDTO.setTileHit(true);
                } else {
                    System.out.println("Target already hit!");
                    fireResultDTO.setTileHit(true);
                }
                checkShipStatus(gameGrid, targetTile,targetedShip);
            } else {
                System.out.println("Error: Unable to retrieve the ship with ID: " + shipId);
            }
        } else {
            System.out.println("Miss! There is no ship at the specified coordinates.");
            fireResultDTO.setTileHit(false);
        }
        return fireResultDTO;
    }

    public void checkShipStatus(grid gameGrid, Tile tile, Ship ship) {
        shipType shipType = ship.getShipType();
        direction shipDirection = ship.getDirection();

        System.out.println("Ship: " + shipType + " " + shipDirection);

        int shipSize = shipType.getSize();
        int x = tile.getX();
        int y = tile.getY();

        boolean isShipSunk = true;

        Tile shipTile;
        List<Tile> shipTiles = new ArrayList<>();

        if (shipDirection.equals(direction.HORIZONTAL_RIGHT) || shipDirection.equals(direction.HORIZONTAL_LEFT)) {
            y=0;
            for (int i = 0; i < gameGrid.getWidth(); i++) {
                shipTile = gameGrid.getTile((x), y + i);
                shipTiles.add(shipTile);
            }
        } else if (shipDirection.equals(direction.VERTICAL_TOP) || shipDirection.equals(direction.VERTICAL_BOTTOM)) {
            x=0;
            for (int i = 0; i < gameGrid.getLength(); i++) {
                shipTile = gameGrid.getTile(x + i, y);
                shipTiles.add(shipTile);
            }
        }

        int shipIdToCheck = ship.getId();
        for (Tile tile1 : shipTiles) {
            System.out.println("Tile id: " + tile1.getId() + " in :" + tile1.getX() + tile1.getY() +", Status: " + tile1.getShipTileStatus() + tile1.getShipId());
            if (tile1.getShipId() == shipIdToCheck) {
                if(tile1.getShipTileStatus()){
                    isShipSunk = false;
                    fireResultDTO.setShipSunk(isShipSunk);
                }
            }
        }

        if (isShipSunk) {
            fireResultDTO.setShipSunk(isShipSunk);
            System.out.println("Ship sunk !");
            ship.setShipStatus(shipStatus.SUNK);
            if(checkGameStatus(gameGrid)){
                System.out.println("Game finish, no more ships are alive !");
                fireResultDTO.setGameFinished(checkGameStatus(gameGrid));
            }
        }
    }

    public boolean checkGameStatus(grid gamegrid) {

        for (int i = 0; i < gamegrid.getLength(); i++) {
            for (int j = 0; j < gamegrid.getWidth(); j++) {
                if(gamegrid.getTile(i,j).getShipTileStatus()) {return false;}
            }
        }
        return true;
    }

    private direction getRandomDirection(Random random) {
        // Get a random direction from the direction enum
        direction[] directions = direction.values();
        return directions[random.nextInt(directions.length)];
    }

    public void resetGame(grid grid) {
        // Reset grid
        grid.resetGrid(shipRepository);
        // Delete all ships
        shipRepository.deleteAll();
        shipRepository.deleteAll();
        System.out.println("Game reset");
    }
}