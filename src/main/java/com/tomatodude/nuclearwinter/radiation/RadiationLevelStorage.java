package com.tomatodude.nuclearwinter.radiation;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class RadiationLevelStorage implements Capability.IStorage<IRadiationCapability> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IRadiationCapability> capability, IRadiationCapability instance, EnumFacing side) {
        return new NBTTagFloat(instance.getRads());
    }

    @Override
    public void readNBT(Capability<IRadiationCapability> capability, IRadiationCapability instance, EnumFacing side, NBTBase nbt) {
        instance.setRads(((NBTPrimitive) nbt).getFloat());
    }
}
