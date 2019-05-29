package com.tomatodude.nuclearwinter.util;

public class Coord2d {

    private int x;
    private int z;

    public Coord2d(int x, int z) {
        this.x = x;
        this.z = z;
    }

    //Lower nibble is x, upper is z
    public Coord2d(int coordPairNumerical){
        this.x = coordPairNumerical & 15;
        this.z = coordPairNumerical >> 4;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
