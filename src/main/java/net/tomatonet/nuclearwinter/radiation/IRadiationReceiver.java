package net.tomatonet.nuclearwinter.radiation;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IRadiationReceiver extends INBTSerializable<CompoundTag> {
    //Override radiation setting
    public void setRads(float rads);
    //Get current radiation
    public float getRads();
    //Add rads to radiation, then returns current rad count
    public float addRads(float rads);
    //Clear all radiation
    public void clearRads();
}
