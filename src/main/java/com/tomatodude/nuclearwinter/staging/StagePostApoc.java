package com.tomatodude.nuclearwinter.staging;

import net.minecraft.world.World;

public class StagePostApoc extends StageBase{
    @Override
    public boolean canDoNextStage(World worldIn) {
        return false;
    }
}
