package com.example.battleships_client;

public class BattleshipsClient {
    private char[][] gridUpdate;
    private String shipStatus;
    private String hitResult;

    public BattleshipsClient() {
    }

    public BattleshipsClient(String hitResult, String shipStatus, char[][] gridUpdated) {
        this.hitResult = hitResult;
        this.shipStatus = shipStatus;
        this.gridUpdate = gridUpdated;
    }

    public char[][] getGridUpdate() {
        return gridUpdate;
    }

    public void setGridUpdate(char[][] gridUpdate) {
        this.gridUpdate = gridUpdate;
    }

    public String getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(String shipStatus) {
        this.shipStatus = shipStatus;
    }

    public String getHitResult() {
        return hitResult;
    }

    public void setHitResult(String hitResult) {
        this.hitResult = hitResult;
    }
}
