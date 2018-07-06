package com.tomatodude.nuclearwinter.staging;

public class StageWorldSettings implements IStageWorldSettings {

    private StageController.STAGES currentStage = StageController.STAGES.INITIAL;
    private boolean isActive = false;
    private double startWorldTick=0;

    @Override
    public void setCurrentStage(StageController.STAGES stage){
        this.currentStage = stage;
    }

    @Override
    public StageController.STAGES getCurrentStage() {
        return currentStage;
    }

    @Override
    public double getTimeInStage() {
        return startWorldTick;
    }

    @Override
    public void setTimeInStage(double worldTickTime) {
        this.startWorldTick=worldTickTime;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }
}
