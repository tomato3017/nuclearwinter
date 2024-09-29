package net.tomatonet.nuclearwinter.staging;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IStageLevelSettings extends INBTSerializable<CompoundTag> {
    void setCurrentStage(StageController.STAGES stage);
    StageController.STAGES getCurrentStage();
    long getStartWorldTime();

    void setStartWorldTime(long levelTickTime);

    boolean isActive();

    void setActive(boolean active);
}
