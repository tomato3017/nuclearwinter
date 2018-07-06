package com.tomatodude.nuclearwinter.util.handler;

import com.tomatodude.nuclearwinter.staging.StageWorldSettingsProvider;
import com.tomatodude.nuclearwinter.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class CapabilitiesHandler {

    @SubscribeEvent
    public static void onCapabilitiesAttach_World(AttachCapabilitiesEvent<World> event){
        World world = event.getObject();
        if(!world.isRemote){
            event.addCapability(new ResourceLocation(Reference.MOD_ID,"staginginfo"),new StageWorldSettingsProvider());
        }
    }
}
