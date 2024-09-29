package net.tomatonet.nuclearwinter.staging;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class StagePlaceholder extends StageBase{
    private static final Logger LOGGER = LogUtils.getLogger();
//    private final ResourceLocation dimKey;
//    private final long worldTickStart;

    public StagePlaceholder(ResourceLocation dimKey, long worldTickStart) {
        super("PLACEHOLDER", null, 0, StageController.STAGES.PREAPOC);
    }

    @Override
    public boolean canDoNextStage(Level levelIn) {
        return false;
    }

    @Override
    public void finalizeStage(Level level) {
        StagePlaceholder.LOGGER.trace("Finalize stage tick for {}", this.getDimKey());
    }

    @Override
    public void initStage(Level level) {
        StagePlaceholder.LOGGER.trace("Init stage tick for {}", this.getDimKey());
    }

    @Override
    public void unloadStage(Level level) {
        StagePlaceholder.LOGGER.trace("Unload stage tick for {}", this.getDimKey());
    }
}
