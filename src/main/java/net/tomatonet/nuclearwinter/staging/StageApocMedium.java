package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.tomatonet.nuclearwinter.Config;
import net.tomatonet.nuclearwinter.radiation.RadiationConfig;

/**
 * Placeholder class for the APOCLOW stage.
 */
public class StageApocMedium extends StageBase {
    public StageApocMedium(ResourceLocation dimKey, long worldTickStart) {
        super(
                StageController.STAGES.APOCMED.toString(),
                dimKey,
                worldTickStart,
                new StageSettings.Builder()
                        .setRadiationLevel(RadiationConfig.RADIATION_EMIT_LEVEL_APOCMED)
                        .setPlayerRadiationEnabled(true)
                        .setNextStageDays(Config.DAYS_APOCALYPSE_MED.get())
                        .build(),
                StageController.STAGES.APOCMED
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