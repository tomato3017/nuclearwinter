package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.tomatonet.nuclearwinter.radiation.RadiationConfig;

/**
 * Stage 3: RECLAMATION — The radiation is peaking and starting to wane. Hope returns.
 * Radiation decreases over time, player radiation decays in safe zones.
 * No fixed duration (indefinite endgame).
 * TODO: Implement decreasing radiation, decay, Rad Scrubber integration in Phase 4.
 */
public class StageReclamation extends StageBase {
    public StageReclamation(ResourceLocation dimKey, long worldTickStart) {
        super(
                StageController.STAGES.RECLAMATION.toString(),
                dimKey,
                worldTickStart,
                new StageSettings.Builder()
                        .setRadiationLevel(RadiationConfig.RADIATION_EMIT_LEVEL_RECLAMATION)
                        .setPlayerRadiationEnabled(true)
                        .setNextStageTicks(0) // Indefinite — no auto-transition
                        .setBlockDegradationEnabled(true)
                        .setDegradationScope(StageSettings.DegradationScope.ALL_CONFIGURED)
                        .setPlayerRadDecayEnabled(true)
                        .setPlayerRadDecayRate(50.0f)
                        .setRadiationLevelDecays(true)
                        .setRadiationLevelMin(RadiationConfig.RADIATION_EMIT_LEVEL_FALLOUT)
                        .setRadiationLevelDecayPerDay(
                                (RadiationConfig.RADIATION_EMIT_LEVEL_RECLAMATION - RadiationConfig.RADIATION_EMIT_LEVEL_FALLOUT) / 7.0f
                        )
                        .setRadiationStormsEnabled(true)
                        .setStormRadiationMultiplier(2.0f)
                        .build(),
                StageController.STAGES.RECLAMATION
        );
    }

    @Override
    public boolean canDoNextStage(Level levelIn) {
        return false; // Indefinite endgame, no next stage
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
