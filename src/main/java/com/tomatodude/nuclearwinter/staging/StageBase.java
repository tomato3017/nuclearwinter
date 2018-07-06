package com.tomatodude.nuclearwinter.staging;

import net.minecraft.world.World;

public abstract class StageBase {

    private String name;

    public String getName(){ return this.name;}

    public abstract boolean canDoNextStage(World worldIn);
}
