package com.tomatodude.nuclearwinter.init;

import java.util.ArrayList;
import java.util.List;

import com.tomatodude.nuclearwinter.blocks.LeadlinedBlock;
import com.tomatodude.nuclearwinter.blocks.WastelandBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	public static final Block WASTELAND_BlOCK = new WastelandBlock("Wasteland_Block",Material.GRASS);
	public static final Block LEADLINED_BLOCK = new LeadlinedBlock("Leadlined_Block",Material.ANVIL);
}
