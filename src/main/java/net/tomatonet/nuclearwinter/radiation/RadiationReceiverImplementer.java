package net.tomatonet.nuclearwinter.radiation;

import net.minecraft.nbt.CompoundTag;

public class RadiationReceiverImplementer implements IRadiationReceiver {
    private float radiationLevel = 0.0f;

    @Override
    public void setRads(float rads) {
        this.radiationLevel = rads;
    }

    @Override
    public float getRads() {
        return radiationLevel;
    }

    @Override
    public float addRads(float rads) {
        float radLevel = radiationLevel;
        radiationLevel = rads + radLevel;

        return radiationLevel;
    }

    @Override
    public void clearRads() {
        setRads(0.0f);
    }

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        tag.putFloat("radiationLevel", radiationLevel);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(nbt.contains("radiationLevel")) {
            this.radiationLevel = nbt.getFloat("radiationLevel");
        } else {
            throw new IllegalArgumentException("Missing 'radiationLevel' in NBT data");
        }
    }
}
