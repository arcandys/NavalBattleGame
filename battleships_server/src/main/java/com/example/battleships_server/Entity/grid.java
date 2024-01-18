package com.example.battleships_server.Entity;

import com.example.battleships_server.repository.ShipRepository;
import org.hibernate.id.factory.IdentifierGeneratorFactory;

import java.util.List;
import java.util.Optional;

public class grid {
    private final Tile[][] tilesGrid;
    private int length;
    private int width;

    public grid(int width, int length) {
        this.length = length;
        this.width = width;
        this.tilesGrid = new Tile[width][length];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                tilesGrid[i][j] = new Tile(i, j);
            }
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Tile[][] getTilesGrid() {
        return tilesGrid;
    }

    public Tile getTile(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return tilesGrid[x][y];
        } else {
            return null;  // Return null for out-of-bounds coordinates
        }
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getLength();
    }

    private boolean canPlaceShip(Ship ship, Tile startTile, grid gameGrid) {
        int x = startTile.getX();
        int y = startTile.getY();
        int size = ship.getShipType().getSize();

        switch (ship.getDirection()) {
            case HORIZONTAL_RIGHT:
                if (x + size > gameGrid.getWidth()) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (!gameGrid.isValidTile(x, y + i)) {
                        return false;
                    }
                    Tile currentTile = gameGrid.getTile(x, y + i);
                    if (currentTile.isOccupied()) {
                        return false;
                    }
                }
                break;

            case HORIZONTAL_LEFT:
                if (y - size + 1 < 0) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (!gameGrid.isValidTile(x, y - i)) {
                        return false;
                    }
                    Tile currentTile = gameGrid.getTile(x, y - i);
                    if (currentTile.isOccupied()) {
                        return false;
                    }
                }
                break;

            case VERTICAL_BOTTOM:
                if (x + size > gameGrid.getLength()) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (!gameGrid.isValidTile(x + i, y)) {
                        return false;
                    }
                    Tile currentTile = gameGrid.getTile(x + i, y);
                    if (currentTile.isOccupied()) {
                        return false;
                    }
                }
                break;

            case VERTICAL_TOP:
                if (x - size + 1 < 0) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (!gameGrid.isValidTile(x - i, y)) {
                        return false;
                    }
                    Tile currentTile = gameGrid.getTile(x - i, y);
                    if (currentTile.isOccupied()) {
                        return false;
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid ship direction.");
        }

        return true;
    }



    public boolean placeShip(Ship ship, Tile startTile, grid gameGrid) {
        if (canPlaceShip(ship, startTile, gameGrid)) {
            int x = startTile.getX();
            int y = startTile.getY();
            int size = ship.getShipType().getSize();

            switch (ship.getDirection()) {
                case HORIZONTAL_RIGHT:
                    for (int i = 0; i < size; i++) {
                        Tile currentTile = gameGrid.getTile(x, y + i);
                        currentTile.setOccupied(true, ship);  // Set occupied and associate with the ship
                    }
                    break;

                case HORIZONTAL_LEFT:
                    for (int i = 0; i < size; i++) {
                        Tile currentTile = gameGrid.getTile(x, y - i);
                        currentTile.setOccupied(true, ship);
                    }
                    break;

                case VERTICAL_BOTTOM:
                    for (int i = 0; i < size; i++) {
                        Tile currentTile = gameGrid.getTile(x + i, y);
                        currentTile.setOccupied(true, ship);
                    }
                    break;

                case VERTICAL_TOP:
                    for (int i = 0; i < size; i++) {
                        Tile currentTile = gameGrid.getTile(x - i, y);
                        currentTile.setOccupied(true, ship);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid ship direction.");
            }

            ship.setIsPlaced(true);
            ship.setOccupiedTiles(startTile.getX(), startTile.getY());
            return true;
        } else {
            System.out.println("Cannot place the ship at the specified location.");
        }
        return false;
    }

    public boolean isValidTile(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < length;
    }

    public void resetGrid(ShipRepository shipRepository) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                Tile tile = tilesGrid[i][j];

                if (tile.isOccupied()) {
                    Optional<Ship> optionalShip = shipRepository.findById(tile.getShipId());
                    optionalShip.ifPresent(ship -> tile.resetOccupied(false, ship)); // Reset the tile
                }
            }
        }
    }
}