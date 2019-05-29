package com.tomatodude.nuclearwinter.staging;

import com.tomatodude.nuclearwinter.NuclearWinter;
import net.minecraft.world.World;

public class StageApocLow extends StageBase {
    public StageApocLow(int dimID, long worldTickStart) {
        super("Apocalypse Low", dimID, worldTickStart);
    }

    @Override
    public boolean canDoNextStage(World worldIn) {
        return false;
    }

    @Override
    public void doStageTick(World worldIn) {
        NuclearWinter.logger.info("ApocLow Stage TICK!!!");
    }

    @Override
    public void finalizeStage(World world) {
        super.finalizeStage(world);
    }

    @Override
    public void initStage(World world) {
        super.initStage(world);
    }

    @Override
    public void unloadStage(World world) {
        super.unloadStage(world);
    }
}
