package com.example.battleships_server.Entity;

import jakarta.persistence.*;

@Entity
public class Tile {
    public int getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int x;

    private int y;

    private boolean occupied;
    private int shipIdTile;

    private boolean shipTileStatus;
    @ManyToOne
    @JoinColumn(name = "ship_id")
    private Ship ship;
    public Tile(){}
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
        this.shipTileStatus = true;
        this.shipIdTile = -1;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied, Ship ship) {
        this.occupied = occupied;
        this.ship = ship;
        this.shipIdTile = ship.getId();
    }


    public boolean getShipTileStatus() {
        return isOccupied() && shipTileStatus;
    }

    public void setShipTileStatus(boolean shipTileStatus) {
        if (isOccupied()) {
            this.shipTileStatus = shipTileStatus;
        } else {
            throw new IllegalStateException("Cannot set shipTileStatus on an unoccupied tile.");
        }
    }

    public void setShipId(Ship ship) {
        this.shipIdTile =  ship.getId();
    }
    public int getShipId () {
        return this.shipIdTile;
    }

    public void resetOccupied(boolean occupied,Ship ship){
        this.setShipTileStatus(true);
        this.ship = null;
        this.shipIdTile = -1;
        this.occupied = occupied;
    }
}