package com.tomatodude.nuclearwinter.staging;

public interface IStageWorldSettings {
    void setCurrentStage(StageController.STAGES stage);
    StageController.STAGES getCurrentStage();
    long getStartWorldTime();

    void setStartWorldTime(long worldTickTime);

    boolean isActive();

    void setActive(boolean active);
}
