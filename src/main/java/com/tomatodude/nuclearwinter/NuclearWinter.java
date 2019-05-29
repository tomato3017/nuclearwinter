package com.tomatodude.nuclearwinter;

import com.tomatodude.nuclearwinter.init.ModCapabilities;
import com.tomatodude.nuclearwinter.init.ModRecipes;
import com.tomatodude.nuclearwinter.proxy.CommonProxy;
import com.tomatodude.nuclearwinter.util.RadiationConfig;
import com.tomatodude.nuclearwinter.util.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
		ModCapabilities.register();
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		ModRecipes.init();
		RadiationConfig.initRadMap();
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {

		
	}
}
