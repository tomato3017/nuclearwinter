package com.tomatodude.nuclearwinter.staging;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StageWorldSettingsProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IStageWorldSettings.class)
    public static Capability<IStageWorldSettings> STAGE_WORLD_SETTINGS_CAPABILITY;

    private IStageWorldSettings instance = STAGE_WORLD_SETTINGS_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == STAGE_WORLD_SETTINGS_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if( capability == STAGE_WORLD_SETTINGS_CAPABILITY){
            return STAGE_WORLD_SETTINGS_CAPABILITY.cast(this.instance);
        }

        return null;
    }

    @Override
    public NBTBase serializeNBT() {
        return STAGE_WORLD_SETTINGS_CAPABILITY.getStorage().writeNBT(STAGE_WORLD_SETTINGS_CAPABILITY,this.instance,null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        STAGE_WORLD_SETTINGS_CAPABILITY.getStorage().readNBT(STAGE_WORLD_SETTINGS_CAPABILITY,this.instance,null,nbt);
    }
}
