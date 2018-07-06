package com.tomatodude.nuclearwinter.staging;

public interface IStageWorldSettings {
    void setCurrentStage(StageController.STAGES stage);
    StageController.STAGES getCurrentStage();
    double getTimeInStage();

    void setTimeInStage(double worldTickTime);

    boolean isActive();

    void setActive(boolean active);
}
