package com.tomatodude.nuclearwinter.radiation;

public class RadiationLevel implements IRadiationCapability {
    private float radiationLevel = 0.0f;

    @Override
    public void setRads(float rads) {
        this.radiationLevel = rads;
    }

    @Override
    public float getRads() {
        return radiationLevel;
    }

    @Override
    public float addRads(float rads) {
        float radLevel = radiationLevel;
        radiationLevel = rads + radLevel;

        return radiationLevel;
    }

    @Override
    public void clearRads() {
        setRads(0.0f);
    }
}
