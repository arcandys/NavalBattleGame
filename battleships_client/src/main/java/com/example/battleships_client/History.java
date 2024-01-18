package com.example.battleships_client;

public class History {

    private int x;
    private int y;
    private Boolean isTileHit;
    private Boolean isShipSunk;

    public History(int x, int y, Boolean isTileHit, Boolean isShipSunk) {
        this.x = x;
        this.y = y;
        this.isTileHit = isTileHit;
        this.isShipSunk = isShipSunk;
    }

    public String getTileCssClass() {
        return isTileHit ? "hit" : "miss";
    }


    public Boolean getShipSunk() {
        return isShipSunk;
    }

    public void setShipSunk(Boolean shipSunk) {
        isShipSunk = shipSunk;
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

    public Boolean getTileHit() {
        return isTileHit;
    }

    public void setTileHit(Boolean tileHit) {
        isTileHit = tileHit;
    }
}
