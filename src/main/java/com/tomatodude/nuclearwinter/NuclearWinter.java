package com.tomatodude.nuclearwinter;

import com.tomatodude.nuclearwinter.init.ModItems;
import com.tomatodude.nuclearwinter.init.ModRecipes;
import com.tomatodude.nuclearwinter.proxy.CommonProxy;
import com.tomatodude.nuclearwinter.util.Reference;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID,name = Reference.NAME, version = Reference.VERSION)
public class NuclearWinter {
	@Instance
	public static NuclearWinter instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS,serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;

	public static final Logger logger = LogManager.getLogger(Reference.NAME);
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event) {
		logger.info(Reference.NAME + " v" + Reference.VERSION + " Initializing!");
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		ModRecipes.init();
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		
	}
}
