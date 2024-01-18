package com.example.battleships_server.Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return id;
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private shipType shipType;
    private shipStatus shipStatus;
    private direction direction;
    private boolean isPlaced;
    private int damageCount;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ship", cascade = CascadeType.ALL)
    private List<Tile> occupiedTiles;

    public shipType getShipType() {
        return shipType;
    }

    public shipStatus getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(shipStatus shipStatus) {
        this.shipStatus = shipStatus;
    }

    public direction getDirection() {
        return direction;
    }

    public void setDirection(com.example.battleships_server.Entity.direction direction) {
        this.direction = direction;
    }

    public void setIsPlaced(boolean isPlaced) {
        this.isPlaced = isPlaced;
    }

    public boolean getIsPlaced() {
        return isPlaced;
    }

    public Ship () {

    }
    public Ship(shipType shipType, direction direction) {
        this.shipType = shipType;
        this.direction = direction;
        this.shipStatus = shipStatus.ALIVE;
        this.isPlaced = false;
        this.occupiedTiles = new ArrayList<>();
        this.damageCount = 0;
    }

    public void setOccupiedTiles(int x, int y) {
        int size = this.shipType.getSize();
        for (int i = 0; i < size; i++) {
            Tile tile = new Tile();
            tile.setOccupied(true, this);
            tile.setShipTileStatus(true);
            tile.setX(x+i);
            tile.setY(y);
            occupiedTiles.add(tile);
        }
        this.damageCount = 0;
    }

    public List<Tile> getOccupiedTiles(){
        return this.occupiedTiles;
    }

    public int getDamageCount() {
        return damageCount;
    }

    public void applyDamage() {
        this.damageCount++;
    }
}
