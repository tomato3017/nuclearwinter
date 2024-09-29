package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Placeholder class for the APOCLOW stage.
 */
public class StageApocLow extends StageBase {
    long ticksTillNextStage = 0;

    public StageApocLow(ResourceLocation dimKey, long worldTickStart, int daysTillNextStage) {
        super(StageController.STAGES.APOCLOW.toString(), dimKey, worldTickStart, StageController.STAGES.APOCLOW);
        ticksTillNextStage = daysTillNextStage * 24000L;
    }

    @Override
    public boolean canDoNextStage(Level levelIn) {
        return false;
    }

    @Override
    public void finalizeStage(Level level) {
        // Placeholder implementation
    }

    @Override
    public void initStage(Level level) {
        // Placeholder implementation
    }

    @Override
    public void unloadStage(Level level) {

    }
}