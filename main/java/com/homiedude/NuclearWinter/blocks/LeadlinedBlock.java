package com.homiedude.NuclearWinter.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class LeadlinedBlock extends BlockBase{
	public LeadlinedBlock(String name, Material material) {
		super(name, material);
		setSoundType(SoundType.ANVIL);
		setHardness(15.0F);
		setResistance(1000.0F);
		setHarvestLevel("pickaxe",3);
		setLightLevel(1.0F);
		
	}
}
