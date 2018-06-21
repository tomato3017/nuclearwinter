package com.tomatodude.nuclearwinter.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class WastelandBlock extends BlockBase {

	public WastelandBlock(String name, Material material) {
		super(name, material);
		setSoundType(SoundType.GROUND);
		setHardness(0.2F);
		setResistance(1.0F);
		setHarvestLevel("shovel",0);
		setLightLevel(1.0F);
		
	}
	

}
