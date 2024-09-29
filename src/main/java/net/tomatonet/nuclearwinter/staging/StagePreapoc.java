package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.tomatonet.nuclearwinter.NuclearWinter;
import org.slf4j.Logger;

/**
 * Placeholder class for the PREAPOC stage.
 */
public class StagePreapoc extends StageBase {
    Logger LOGGER = NuclearWinter.LOGGER;
    long ticksTillNextStage = 0;

    public StagePreapoc(ResourceLocation dimKey, long worldTickStart, int daysTillNextStage) {
        super(StageController.STAGES.PREAPOC.toString(), dimKey, worldTickStart, StageController.STAGES.PREAPOC);
        ticksTillNextStage = daysTillNextStage * 24000L;
    }

    @Override
    public boolean canDoNextStage(Level levelIn) {
        if (ticksTillNextStage != 0 && levelIn.getGameTime() - this.getWorldTickStart() >= ticksTillNextStage) {
            LOGGER.trace("Next stage tick count hit for {}", this.getDimKey().toString());
            return true;
        }

        return false;
    }
}