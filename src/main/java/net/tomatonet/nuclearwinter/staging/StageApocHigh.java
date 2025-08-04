package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.tomatonet.nuclearwinter.radiation.RadiationConfig;

/**
 * Placeholder class for the APOCLOW stage.
 */
public class StageApocHigh extends StageBase {
    public StageApocHigh(ResourceLocation dimKey, long worldTickStart) {
        super(
                StageController.STAGES.APOCHIGH.toString(),
                dimKey,
                worldTickStart,
                new StageSettings.Builder()
                        .setRadiationLevel(RadiationConfig.RADIATION_EMIT_LEVEL_APOCHIGH)
                        .setPlayerRadiationEnabled(true)
                        .setNextStageTicks(0)
                        .build(),
                StageController.STAGES.APOCHIGH
        );

    }

    @Override
    public void doStageTick(Level level) {
        super.doStageTick(level);
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