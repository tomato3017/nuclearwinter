package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.tomatonet.nuclearwinter.Config;
import net.tomatonet.nuclearwinter.radiation.RadiationConfig;

/**
 * Stage 1: FALLOUT — The bombs have fallen. Radiation rains from the sky.
 * Organic blocks degrade, weather is locked to overcast, radiation is moderate.
 * TODO: Implement weather locking, organic-only degradation in Phase 2.
 */
public class StageFallout extends StageBase {
    public StageFallout(ResourceLocation dimKey, long worldTickStart) {
        super(
                StageController.STAGES.FALLOUT.toString(),
                dimKey,
                worldTickStart,
                new StageSettings.Builder()
                        .setRadiationLevel(RadiationConfig.RADIATION_EMIT_LEVEL_FALLOUT)
                        .setPlayerRadiationEnabled(true)
                        .setNextStageDays(Config.DAYS_FALLOUT.get())
                        .setBlockDegradationEnabled(true)
                        .setDegradationScope(StageSettings.DegradationScope.ORGANIC_ONLY)
                        .setWeatherLocked(true)
                        .setLockedWeather(StageSettings.LockedWeather.OVERCAST)
                        .build(),
                StageController.STAGES.FALLOUT
        );
    }

    @Override
    public void initStage(Level level) {
        super.initStage(level);
    }

    @Override
    public void finalizeStage(Level level) {
        super.finalizeStage(level);
    }
}
