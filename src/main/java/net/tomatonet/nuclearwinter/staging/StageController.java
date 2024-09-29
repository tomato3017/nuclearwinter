package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tomatonet.nuclearwinter.Config;
import net.tomatonet.nuclearwinter.NuclearWinter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.InvalidClassException;
import java.util.HashMap;


public class StageController {
    private final Logger LOGGER = NuclearWinter.LOGGER;
    private HashMap<ResourceLocation, StageBase> activeStageMap = new HashMap<>();

    public StageController() {

    }

    public boolean isLoadedStage(Level level) {
        ResourceLocation dimKey = level.dimension().location();
        return activeStageMap.containsKey(dimKey);
    }

    @NotNull
    public StageBase getActiveStage(Level level) {
        ResourceLocation dimKey = level.dimension().location();
        return activeStageMap.get(dimKey);
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        LOGGER.debug("World loaded");

        if (!event.getLevel().isClientSide() && event.getLevel() instanceof Level level) {
            if (!hasStageLevelSettings(level)) {
                LOGGER.warn("Stage level settings not found for " + level.dimension().location());
                return;
            }
            IStageLevelSettings stageLevelSettings = getStageLevelSettings(level);
            if (!stageLevelSettings.isActive()) {
                LOGGER.debug("Staging not active for " + level.dimension().location());
                return;
            }

            LOGGER.debug("Activating staging for " + level.dimension().location());
            this.loadStage(level, stageLevelSettings);
        }
    }

    public void activateStaging(Level level, STAGES stageType) {
        if (!level.isClientSide() && hasStageLevelSettings(level)) {
            IStageLevelSettings stageLevelSettings = getStageLevelSettings(level);
            LOGGER.debug("Activating staging for " + level.dimension().location());

            stageLevelSettings.setActive(true);
            stageLevelSettings.setCurrentStage(stageType);
            stageLevelSettings.setStartWorldTime(level.getGameTime());
            this.loadStage(level, stageLevelSettings);

            return;
        }

        throw new IllegalStateException("Level is client side or does not have stage level settings");
    }

    public void deactivateStaging(Level level) {
        if (!level.isClientSide() && hasStageLevelSettings(level)) {
            IStageLevelSettings stageLevelSettings = getStageLevelSettings(level);
            LOGGER.debug("Deactivating staging for " + level.dimension().location());

            stageLevelSettings.setActive(false);
            activeStageMap.remove(level.dimension().location());
            return;
        }

        throw new IllegalStateException("Level is client side or does not have stage level settings");
    }

    @SubscribeEvent
    public void onCapabilityAttach(AttachCapabilitiesEvent<Level> event) {
        if (!event.getObject().isClientSide()) {
            if (!hasStageLevelSettings(event.getObject())) {
                LOGGER.debug("Creating stage level settings for " + event.getObject().dimension().location());
                StageLevelSettingsAttacher.attach(event);
            }
        }
    }

    public boolean hasStageLevelSettings(Level level) {
        return level.getCapability(StageLevelSettings.INSTANCE, null).isPresent();
    }

    public IStageLevelSettings getStageLevelSettings(Level level) {
        return level.getCapability(StageLevelSettings.INSTANCE, null).
                orElseThrow(() -> new IllegalStateException("Stage level settings not found"));
    }

    private StageBase getCurrentStageFromWorld(Level level, IStageLevelSettings stageLevelSettings) throws InvalidClassException {
        ResourceKey<Level> dimKey = level.dimension(); //Get the dimension

        long worldTickStart = stageLevelSettings.getStartWorldTime();

        return switch (stageLevelSettings.getCurrentStage()) {
            case PREAPOC -> new StagePreapoc(dimKey.location(), worldTickStart, Config.DAYS_BEFORE_APOCALYPSE.get());
            case APOCLOW -> new StageApocLow(dimKey.location(), worldTickStart, Config.DAYS_APOCALYPSE_LOW.get());
            case APOCMED -> new StagePlaceholder(dimKey.location(), worldTickStart);
            case APOCHIGH -> new StagePlaceholder(dimKey.location(), worldTickStart);
            case POSTAPOC -> new StagePlaceholder(dimKey.location(), worldTickStart);
        };
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof Level level) {
            if (isLoadedStage(level)) {
                LOGGER.debug("Unloading stage for " + level.dimension().location());
                StageBase stage = getActiveStage(level);
                stage.unloadStage(level);

                activeStageMap.remove(level.dimension().location());
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && isLoadedStage(event.level)) {
            Level level = event.level;
            LOGGER.trace("Level tick for {}", level.dimension().location());

            StageBase stage = activeStageMap.get(event.level.dimension().location());
            if (stage.getNextTick() <= event.level.getGameTime()) {
                tickStage(stage, level);
            }
        }
    }

    private void tickStage(StageBase stage, Level level) {
        LOGGER.debug("Stage tick for {} for level {}",stage.getName(), level.dimension().location());
        stage.doStageTick(level);

        //Were going to put the next stage logic here,
        //it'll be locked to the stage tick itself but stop from being called every world tick
        if(stage.canDoNextStage(level)){
            LOGGER.debug("Can do next stage for {}", stage.getName());
            LOGGER.debug("Finalizing stage {}", stage.getName());
            stage.finalizeStage(level);
            StageController.STAGES nextStage = STAGES.valueOf(stage.getStageType().value + 1);

            activateStaging(level, nextStage);
        }
    }

    private StageBase loadStage(Level level, IStageLevelSettings stageLevelSettings) {
        StageBase stage;
        try {
            stage = getCurrentStageFromWorld(level, stageLevelSettings);
        } catch (InvalidClassException e) {
            e.printStackTrace();
            return null;
        }

        activeStageMap.put(stage.getDimKey(), stage);
        stage.initStage(level);
        return stage;
    }

    public enum STAGES {
        PREAPOC(0), APOCLOW(1), APOCMED(2), APOCHIGH(3), POSTAPOC(4);

        private final static HashMap<Integer, STAGES> map = new HashMap<>();

        static {
            for (STAGES stage : STAGES.values()) {
                map.put(stage.value, stage);
            }
        }

        private final int value;

        STAGES(int i) {
            this.value = i;
        }

        public static STAGES valueOf(int stageNum) {
            STAGES stage = map.get(stageNum);
            if (stage == null) {
                throw new IllegalArgumentException("Invalid stage number: " + stageNum);
            }
            return map.get(stageNum);
        }

        public int getValue() {
            return value;
        }

    }
}
