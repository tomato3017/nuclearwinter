package net.tomatonet.nuclearwinter.staging;

import net.minecraft.nbt.CompoundTag;

public class StageLevelSettingsImplementer implements IStageLevelSettings {

    private StageController.STAGES currentStage = StageController.STAGES.PREAPOC;
    private boolean isActive = false;
    private long startWorldTick=0;

    @Override
    public void setCurrentStage(StageController.STAGES stage){
        this.currentStage = stage;
    }

    @Override
    public StageController.STAGES getCurrentStage() {
        return currentStage;
    }

    @Override
    public long getStartWorldTime() {
        return startWorldTick;
    }

    @Override
    public void setStartWorldTime(long worldTickTime) {
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

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("currentStage", currentStage.name());
        tag.putBoolean("isActive", isActive);
        tag.putLong("startWorldTick", startWorldTick);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (!nbt.contains("currentStage")) {
            throw new IllegalArgumentException("Missing 'currentStage' in NBT data");
        }
        this.currentStage = StageController.STAGES.valueOf(nbt.getString("currentStage"));

        if (!nbt.contains("isActive")) {
            throw new IllegalArgumentException("Missing 'isActive' in NBT data");
        }
        this.isActive = nbt.getBoolean("isActive");

        if (!nbt.contains("startWorldTick")) {
            throw new IllegalArgumentException("Missing 'startWorldTick' in NBT data");
        }
        this.startWorldTick = nbt.getLong("startWorldTick");
    }
}
