package net.tomatonet.nuclearwinter.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tomatonet.nuclearwinter.NuclearWinter;
import net.tomatonet.nuclearwinter.radiation.IRadiationReceiver;
import net.tomatonet.nuclearwinter.radiation.RadiationReceiver;
import net.tomatonet.nuclearwinter.radiation.RadiationReceiverAttacher;
import net.tomatonet.nuclearwinter.staging.IStageLevelSettings;
import net.tomatonet.nuclearwinter.staging.StageLevelSettings;
import net.tomatonet.nuclearwinter.staging.StageLevelSettingsAttacher;

public class CapabiltiesAttacher {
    @SubscribeEvent
    public static void onCapabilityAttachLevel(AttachCapabilitiesEvent<Level> event) {
        if (!event.getObject().isClientSide()) {
            if (!hasStageLevelSettings(event.getObject())) {
                NuclearWinter.LOGGER.debug("Creating stage level settings for " + event.getObject().dimension().location());
                StageLevelSettingsAttacher.attach(event);
            }
        }
    }

    public static boolean hasStageLevelSettings(Level level) {
        return level.getCapability(StageLevelSettings.INSTANCE, null).isPresent();
    }

    public static IStageLevelSettings getStageLevelSettings(Level level) {
        return level.getCapability(StageLevelSettings.INSTANCE, null).
                orElseThrow(() -> new IllegalStateException("Stage level settings not found"));
    }

    @SubscribeEvent
    public static void onCapabilityAttachPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player player){
            if (!hasRadiationSettings(player)) {
                RadiationReceiverAttacher.attach(event);
            }
        }
    }

    public static boolean hasRadiationSettings(Player player) {
        return player.getCapability(RadiationReceiver.INSTANCE, null).isPresent();
    }

    public static IRadiationReceiver getRadiationSettings(Player player) {
        return player.getCapability(RadiationReceiver.INSTANCE, null).
                orElseThrow(() -> new IllegalStateException("Radiation settings not found"));
    }
}
