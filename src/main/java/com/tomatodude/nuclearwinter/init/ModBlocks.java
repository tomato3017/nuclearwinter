package com.tomatodude.nuclearwinter.init;

import com.tomatodude.nuclearwinter.NuclearWinter;
import com.tomatodude.nuclearwinter.blocks.LeadlinedBlock;
import com.tomatodude.nuclearwinter.blocks.WastelandBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
	public static final List<Block> BLOCKS = new ArrayList<>();
	public static final Block WASTELAND_BlOCK = new WastelandBlock("Wasteland_Block",Material.GRASS);
	public static final Block LEADLINED_BLOCK = new LeadlinedBlock("Leadlined_Block",Material.ANVIL);

	public static void registerItems(RegistryEvent.Register<Block> event){
		for (Block b : BLOCKS) {
			NuclearWinter.logger.info("Registering block " + b.getUnlocalizedName());
			event.getRegistry().register(b);
		}
	}
}
