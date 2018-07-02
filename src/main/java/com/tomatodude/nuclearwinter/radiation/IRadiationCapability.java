package com.tomatodude.nuclearwinter.radiation;

public interface IRadiationCapability {

    //Override radiation setting
    public void setRads(float rads);
    //Get current radiation
    public float getRads();
    //Add rads to radiation, then returns current rad count
    public float addRads(float rads);
    //Clear all radiation
    public void clearRads();
}
