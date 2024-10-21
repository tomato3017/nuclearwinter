package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.tomatonet.nuclearwinter.Config;
import net.tomatonet.nuclearwinter.NuclearWinter;
import net.tomatonet.nuclearwinter.capabilities.CapabiltiesAttacher;
import net.tomatonet.nuclearwinter.radiation.RadiationReceiver;
import net.tomatonet.nuclearwinter.radiation.RadiationReceiverAttacher;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.InvalidClassException;
import java.util.HashMap;


public class StageController {
    private final Logger LOGGER = NuclearWinter.LOGGER;
    private final HashMap<ResourceLocation, StageBase> activeStageMap = new HashMap<>();

    public StageController() {

    }

    public static String getStageNames() {
        StringBuilder sb = new StringBuilder();
        for (STAGES stage : STAGES.values()) {
            sb.append(stage.name()).append(", ");
        }

        return sb.toString();
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

    public void activateStaging(Level level, STAGES stageType) {
        if (!level.isClientSide() && CapabiltiesAttacher.hasStageLevelSettings(level)) {
            IStageLevelSettings stageLevelSettings = CapabiltiesAttacher.getStageLevelSettings(level);
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
        if (!level.isClientSide() && CapabiltiesAttacher.hasStageLevelSettings(level)) {
            IStageLevelSettings stageLevelSettings = CapabiltiesAttacher.getStageLevelSettings(level);
            LOGGER.debug("Deactivating staging for " + level.dimension().location());

            stageLevelSettings.setActive(false);
            activeStageMap.remove(level.dimension().location());
            return;
        }

        throw new IllegalStateException("Level is client side or does not have stage level settings");
    }

    private StageBase getCurrentStageFromWorld(Level level, IStageLevelSettings stageLevelSettings) throws InvalidClassException {
        ResourceKey<Level> dimKey = level.dimension(); //Get the dimension

        long worldTickStart = stageLevelSettings.getStartWorldTime();

        return switch (stageLevelSettings.getCurrentStage()) {
            case PREAPOC -> new StagePreapoc(dimKey.location(), worldTickStart).
                    withDaysTillNextStage(Config.DAYS_BEFORE_APOCALYPSE.get());
            case APOCLOW -> new StageApocLow(dimKey.location(), worldTickStart).
                    withDaysTillNextStage(Config.DAYS_APOCALYPSE_LOW.get());
            case APOCMED -> new StagePlaceholder(dimKey.location(), worldTickStart);
            case APOCHIGH -> new StagePlaceholder(dimKey.location(), worldTickStart);
            case POSTAPOC -> new StagePlaceholder(dimKey.location(), worldTickStart);
        };
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        LOGGER.debug("World loading");

        if (!event.getLevel().isClientSide() && event.getLevel() instanceof Level level) {
            if (!CapabiltiesAttacher.hasStageLevelSettings(level)) {
                LOGGER.warn("Stage level settings not found for " + level.dimension().location());
                return;
            }
            IStageLevelSettings stageLevelSettings = CapabiltiesAttacher.getStageLevelSettings(level);
            if (!stageLevelSettings.isActive()) {
                LOGGER.debug("Staging not active for " + level.dimension().location());
                return;
            }

            LOGGER.debug("Loading staging for {} ", level.dimension().location());
            StageBase stage = loadStage(level, stageLevelSettings);
            LOGGER.debug("Loaded stage {} for {}", stage.getName(), level.dimension().location());
        }
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

            StageBase stage = activeStageMap.get(event.level.dimension().location());
            if (stage.getNextTick() <= event.level.getGameTime()) {
                tickStage(stage, level);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START &&
                event.side == LogicalSide.SERVER &&
                isLoadedStage(event.player.level()) &&
                event.player.level().getGameTime() % 20 == 0) { // Tick every second
            StageBase stage = activeStageMap.get(event.player.level().dimension().location());
            stage.doPlayerTick(event.player);
        }
    }

    private void tickStage(StageBase stage, Level level) {
        stage.doStageTick(level);

        //Were going to put the next stage logic here,
        //it'll be locked to the stage tick itself but stop from being called every world tick
        if (stage.canDoNextStage(level)) {
            LOGGER.debug("Can do next stage for {}", stage.getName());
            LOGGER.debug("Finalizing stage {}", stage.getName());
            stage.finalizeStage(level);
            StageController.STAGES nextStage = STAGES.valueOf(stage.getStageType().value + 1);

            activateStaging(level, nextStage);
        }
    }

    @NotNull
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
