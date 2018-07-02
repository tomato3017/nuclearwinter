package com.tomatodude.nuclearwinter.util.handler;

import com.tomatodude.nuclearwinter.NuclearWinter;
import com.tomatodude.nuclearwinter.effect.PotionRadiation;
import com.tomatodude.nuclearwinter.init.ModBlocks;
import com.tomatodude.nuclearwinter.init.ModItems;
import com.tomatodude.nuclearwinter.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {
	public static final PotionRadiation radiation = new PotionRadiation("Radiation", "radiation", 1.0d);;

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		NuclearWinter.logger.info("Registering Items");
		ModItems.registerItems(event);
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		NuclearWinter.logger.info("Registering Blocks");
		ModBlocks.registerItems(event);
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for(Item item : ModItems.ITEMS) {
			if(item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
		}
		for(Block block : ModBlocks.BLOCKS) {
			if(block instanceof IHasModel) {
				((IHasModel)block).registerModels();
			}
		}
	
	}

	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<Potion> event){
		event.getRegistry().registerAll(radiation);
	}
}
