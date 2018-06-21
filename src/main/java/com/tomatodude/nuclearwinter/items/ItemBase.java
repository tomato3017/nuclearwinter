package com.tomatodude.nuclearwinter.items;

import com.tomatodude.nuclearwinter.NuclearWinter;
import com.tomatodude.nuclearwinter.init.ModItems;
import com.tomatodude.nuclearwinter.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
	
	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.TOOLS);
		
		ModItems.ITEMS.add(this);
	}
	@Override
	public void registerModels() {
		NuclearWinter.proxy.registerItemRenderer(this,0,"inventory");
		
	}

}
