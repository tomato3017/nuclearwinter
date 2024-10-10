package net.tomatonet.nuclearwinter.radiation;

public class RadiationSettings {

    private boolean playerEffected = false;
    private boolean degradeBlocks = true;
    private boolean blockLightDegradation = false;

    private float startRadLevel = 65535; //TODO: Configify this

    public RadiationSettings() {
        this.setStartRadLevel(RadiationConfig.MAX_RADIATION_LEVEL);
    }

    public boolean isPlayerEffected() {
        return playerEffected;
    }

    public RadiationSettings setPlayerEffected(boolean playerEffected) {
        this.playerEffected = playerEffected;
        return this;
    }

    public boolean isDegradeBlocks() {
        return degradeBlocks;
    }

    public RadiationSettings setDegradeBlocks(boolean degradeBlocks) {
        this.degradeBlocks = degradeBlocks;
        return this;
    }

    //TODO: Enable getting from stage?
    public float getStartRadLevel() {
        return startRadLevel;
    }

    public RadiationSettings setStartRadLevel(float startRadLevel) {
        this.startRadLevel = startRadLevel;
        return this;
    }

    public boolean isBlockLightDegradation() {
        if(isPlayerEffected()){
            return true;
        } else {
            return blockLightDegradation;
        }
    }

    public RadiationSettings setBlockLightDegradation(boolean blockLightDegradation) {
        this.blockLightDegradation = blockLightDegradation;
        return this;
    }
}
