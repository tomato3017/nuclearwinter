package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.tomatonet.nuclearwinter.Config;
import net.tomatonet.nuclearwinter.radiation.RadiationConfig;

/**
 * Stage 2: WASTELAND — The surface is dead. Radiation is lethal.
 * Full block degradation, radiation storms, weather locked to storm.
 * TODO: Implement storm logic, RadChunkProcessor, hazmat integration in Phase 3.
 */
public class StageWasteland extends StageBase {
    public StageWasteland(ResourceLocation dimKey, long worldTickStart) {
        super(
                StageController.STAGES.WASTELAND.toString(),
                dimKey,
                worldTickStart,
                new StageSettings.Builder()
                        .setRadiationLevel(RadiationConfig.RADIATION_EMIT_LEVEL_WASTELAND)
                        .setPlayerRadiationEnabled(true)
                        .setNextStageDays(Config.DAYS_WASTELAND.get())
                        .setBlockDegradationEnabled(true)
                        .setDegradationScope(StageSettings.DegradationScope.ALL_CONFIGURED)
                        .setWeatherLocked(true)
                        .setLockedWeather(StageSettings.LockedWeather.STORM)
                        .setRadiationStormsEnabled(true)
                        .setStormRadiationMultiplier(2.0f)
                        .build(),
                StageController.STAGES.WASTELAND
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
