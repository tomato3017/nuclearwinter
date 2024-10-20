package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.tomatonet.nuclearwinter.radiation.RadiationSettings;

/**
 * Placeholder class for the APOCLOW stage.
 */
public class StageApocLow extends StageBase {
    public StageApocLow(ResourceLocation dimKey, long worldTickStart) {
        super(
                StageController.STAGES.APOCLOW.toString(),
                dimKey,
                worldTickStart,
                new StageSettings.Builder()
                        .setRadiationLevel(1000.0f)
                        .setPlayerRadiationEnabled(true)
                        .build(),
                StageController.STAGES.APOCLOW
        );
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