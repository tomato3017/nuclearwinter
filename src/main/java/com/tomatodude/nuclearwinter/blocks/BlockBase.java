package com.tomatodude.nuclearwinter.blocks;

import com.tomatodude.nuclearwinter.NuclearWinter;
import com.tomatodude.nuclearwinter.init.ModBlocks;
import com.tomatodude.nuclearwinter.init.ModItems;
import com.tomatodude.nuclearwinter.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel {
	public BlockBase(String name, Material material) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels() {
		NuclearWinter.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}


}
