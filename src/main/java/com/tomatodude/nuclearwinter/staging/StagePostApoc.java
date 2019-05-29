package com.tomatodude.nuclearwinter.staging;

import net.minecraft.world.World;

public class StagePostApoc extends StageBase{
    public StagePostApoc(int dimID, long worldTickStart) {
        super("Post Apocalpyse", dimID, worldTickStart);
    }

    @Override
    public boolean canDoNextStage(World worldIn) {
        return false;
    }
}
