package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.tomatonet.nuclearwinter.NuclearWinter;
import net.tomatonet.nuclearwinter.radiation.RadiationSettings;
import org.slf4j.Logger;

/**
 * Placeholder class for the PREAPOC stage.
 */
public class StagePreapoc extends StageBase {
    Logger LOGGER = NuclearWinter.LOGGER;
    public StagePreapoc(ResourceLocation dimKey, long worldTickStart) {
        super(StageController.STAGES.PREAPOC.toString(), dimKey, worldTickStart, new StageSettings.Builder().build(), StageController.STAGES.PREAPOC);
    }

}