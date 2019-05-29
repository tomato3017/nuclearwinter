package com.tomatodude.nuclearwinter.staging;

import net.minecraft.world.World;

public class StageApocMed extends StageBase {
    public StageApocMed(int dimID, long worldTickStart) {
        super("Apocalplyse Medium", dimID, worldTickStart);
    }

    @Override
    public boolean canDoNextStage(World worldIn) {
        return false;
    }
}
