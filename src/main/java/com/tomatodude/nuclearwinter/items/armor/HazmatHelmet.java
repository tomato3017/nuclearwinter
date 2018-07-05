package com.tomatodude.nuclearwinter.items.armor;

import com.tomatodude.nuclearwinter.util.RadiationConfig;
import net.minecraft.inventory.EntityEquipmentSlot;

public class HazmatHelmet extends ArmorBase{

    public HazmatHelmet(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(name, materialIn, renderIndexIn, equipmentSlotIn);
    }

    @Override
    public float getRadResistance() {
        return RadiationConfig.HAZMAT_HELMET_RESISTANCE;
    }
}
