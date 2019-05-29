package com.tomatodude.nuclearwinter.staging;

import net.minecraft.world.World;

public class StageApocHigh extends StageBase {
    public StageApocHigh(int dimID, long worldTickStart) {
        super("Apocalypse High", dimID, worldTickStart);
    }

    @Override
    public boolean canDoNextStage(World worldIn) {
        return false;
    }
}
