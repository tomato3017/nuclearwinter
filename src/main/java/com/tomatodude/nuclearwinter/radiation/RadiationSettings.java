package com.tomatodude.nuclearwinter.radiation;

import com.tomatodude.nuclearwinter.util.RadiationConfig;

public class RadiationSettings {

    private boolean playerEffected = false;
    private boolean degradeBlocks = true;
    private boolean blockLightDegradation = false;

    private float startRadLevel;

    public RadiationSettings() {
        this.setStartRadLevel(RadiationConfig.MAX_RADIATION_LEVEL);
    }

    public boolean isPlayerEffected() {
        return playerEffected;
    }

    public void setPlayerEffected(boolean playerEffected) {
        this.playerEffected = playerEffected;
    }

    public boolean isDegradeBlocks() {
        return degradeBlocks;
    }

    public void setDegradeBlocks(boolean degradeBlocks) {
        this.degradeBlocks = degradeBlocks;
    }

        //TODO: Enable getting from stage?
    public float getStartRadLevel() {
        return startRadLevel;
    }

    public void setStartRadLevel(float startRadLevel) {
        this.startRadLevel = startRadLevel;
    }

    public boolean isBlockLightDegradation() {
        if(isPlayerEffected()){
            return true;
        } else {
            return blockLightDegradation;
        }
    }

    public void setBlockLightDegradation(boolean blockLightDegradation) {
        this.blockLightDegradation = blockLightDegradation;
    }
}
