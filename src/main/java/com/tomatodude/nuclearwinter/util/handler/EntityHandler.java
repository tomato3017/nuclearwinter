package com.tomatodude.nuclearwinter.util.handler;

import com.tomatodude.nuclearwinter.radiation.IRadiationCapability;
import com.tomatodude.nuclearwinter.radiation.RadiationLevelProvider;
import com.tomatodude.nuclearwinter.util.RadiationConfig;
import com.tomatodude.nuclearwinter.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.tomatodude.nuclearwinter.util.Util.applyEffectIfReady;

@EventBusSubscriber
public class EntityHandler {

    @SubscribeEvent
    public static void onPlayerTick(LivingEvent.LivingUpdateEvent event){
        if(event.getEntityLiving() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            //Check for Radiation
            if(player.hasCapability(RadiationLevelProvider.RADIATION_LEVEL_CAPABILITY,null)){
                IRadiationCapability radCap = player.getCapability(RadiationLevelProvider.RADIATION_LEVEL_CAPABILITY, null);

                final float rads = radCap.getRads();
                if(rads >= RadiationConfig.RADIATION3_RAD_LEVEL){
                    applyEffectIfReady(player, RegistryHandler.radiation, 2, 1800);
                } else if (rads >= RadiationConfig.RADIATION2_RAD_LEVEL){
                    applyEffectIfReady(player, RegistryHandler.radiation, 1, 1800);
                } else if (rads >= RadiationConfig.RADIATION1_RAD_LEVEL){
                    applyEffectIfReady(player, RegistryHandler.radiation, 0, 1800);
                }
            }
        }

    }

    @SubscribeEvent
    public static void onCapabilitiesAttach_Entity(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.getObject();

            if(!player.getEntityWorld().isRemote){
                event.addCapability(new ResourceLocation(Reference.MOD_ID,"RadiationInfo"), new RadiationLevelProvider());
            }
        }
    }
}
