package com.tomatodude.nuclearwinter.staging;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class StageWorldSettingsStorage implements Capability.IStorage<IStageWorldSettings> {

    private static final String IS_ACTIVE = "isActive";
    private static final String CURRENT_STAGE = "currentStage";
    private static final String START_WORLD_TICK = "startWorldTick";

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IStageWorldSettings> capability, IStageWorldSettings instance, EnumFacing side) {
        NBTTagCompound nbtComp = new NBTTagCompound();
        nbtComp.setBoolean(IS_ACTIVE, instance.isActive());
        nbtComp.setInteger(CURRENT_STAGE, instance.getCurrentStage().getValue());
        nbtComp.setLong(START_WORLD_TICK, instance.getStartWorldTime());

        return nbtComp;
    }

    @Override
    public void readNBT(Capability<IStageWorldSettings> capability, IStageWorldSettings instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound nbtComp = (NBTTagCompound) nbt;
        instance.setActive(nbtComp.getBoolean(IS_ACTIVE));
        instance.setCurrentStage(StageController.STAGES.valueOf(nbtComp.getInteger(CURRENT_STAGE)));
        instance.setStartWorldTime(nbtComp.getLong(START_WORLD_TICK));
    }
}
