package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Placeholder class for the APOCLOW stage.
 */
public class StageApocLow extends StageBase {
    public StageApocLow(ResourceLocation dimKey, long worldTickStart) {
        super(StageController.STAGES.APOCLOW.toString(), dimKey, worldTickStart, StageController.STAGES.APOCLOW);
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