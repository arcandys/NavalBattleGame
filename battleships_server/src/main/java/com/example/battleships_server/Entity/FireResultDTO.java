package com.example.battleships_server.Entity;

public class FireResultDTO {
    private boolean isTileHit;
    private boolean isShipSunk;

    public void setTileHit(boolean tileHit) {
        isTileHit = tileHit;
    }

    public void setShipSunk(boolean shipSunk) {
        isShipSunk = shipSunk;
    }

    public void setGameFinished(boolean gameFinished) {
        isGameFinished = gameFinished;
    }

    private boolean isGameFinished;

    public FireResultDTO(){

    }
    public FireResultDTO(boolean isTileHit, boolean isShipSunk, boolean isGameFinished) {
        this.isTileHit = isTileHit;
        this.isShipSunk = isShipSunk;
        this.isGameFinished = isGameFinished;
    }

    public boolean isTileHit() {
        return isTileHit;
    }

    public boolean isShipSunk() {
        return isShipSunk;
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }
}
