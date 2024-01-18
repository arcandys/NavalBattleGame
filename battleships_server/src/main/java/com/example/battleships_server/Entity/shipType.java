package com.example.battleships_server.Entity;

public enum shipType {
    AIRCRAFT_CARRIER(4),
    CRUISER(3),
    DESTROYER(2),
    TORPEDO_BOAT(1);

    private final int size;

    shipType(int size) {
        this.size = size;
    }
    public int getSize(){
        return this.size;
    }
}