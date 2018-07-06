package com.tomatodude.nuclearwinter.staging;

import net.minecraft.world.World;

public class StagePreapoc extends StageBase{

    public StagePreapoc() {
    }

    @Override
    public boolean canDoNextStage(World worldIn) {
        return false;
    }
}
