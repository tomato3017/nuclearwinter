package com.tomatodude.nuclearwinter.radiation;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class RadiationLevelProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IRadiationCapability.class)
    public static Capability<IRadiationCapability> RADIATION_LEVEL_CAPABILITY;

    private IRadiationCapability instance = RADIATION_LEVEL_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == RADIATION_LEVEL_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if( capability == RADIATION_LEVEL_CAPABILITY){
            return RADIATION_LEVEL_CAPABILITY.cast(this.instance);
        }

        return null;
    }

    @Override
    public NBTBase serializeNBT() {
        return RADIATION_LEVEL_CAPABILITY.getStorage().writeNBT(RADIATION_LEVEL_CAPABILITY, this.instance,null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        RADIATION_LEVEL_CAPABILITY.getStorage().readNBT(RADIATION_LEVEL_CAPABILITY, this.instance, null, nbt);
    }
}
