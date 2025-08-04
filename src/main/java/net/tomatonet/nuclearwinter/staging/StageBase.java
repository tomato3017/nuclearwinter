package net.tomatonet.nuclearwinter.staging;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.tomatonet.nuclearwinter.NuclearWinter;
import net.tomatonet.nuclearwinter.capabilities.CapabiltiesAttacher;
import net.tomatonet.nuclearwinter.radiation.*;

public abstract class StageBase {
    private final StageSettings settings;
    private final String name;
    private final ResourceLocation dimKey;
    private final StageController.STAGES stageType;
    private final long worldTickStart;

    private long tickTime; //The time of the last tick

    //Stage Settings

    public StageBase(String name, ResourceLocation dimKey, long worldTickStart, StageSettings settings, StageController.STAGES stageType) {
        this.name = name;
        this.dimKey = dimKey;
        this.worldTickStart = worldTickStart;
        this.stageType = stageType;
        this.settings = settings;
    }

    public ResourceLocation getDimKey() {
        return dimKey;
    }

    public long getWorldTickStart() {
        return worldTickStart;
    }

    public void doPlayerTick(Player player){
        NuclearWinter.LOGGER.trace("Player Tick {} for {}", name, dimKey.toString());
        if (settings.isPlayerRadiationEnabled()) {
            processPlayerRadiation(player);
        }
    }

    private void processPlayerRadiation(Player player) {
        if(CapabiltiesAttacher.hasRadiationSettings(player)){
            IRadiationReceiver playerRadCap = CapabiltiesAttacher.getRadiationSettings(player);
            RadiationSettings radSettings = new RadiationSettings().
                    setInitialRadLevel(settings.getRadiationLevel()).
                    setPlayerEffected(true).
                    setDegradeBlocks(false);

            RadiationSource radSource = new RadiationSource(radSettings);
            Vec3 startPos = RadiationSource.getSkyPos(player.level(), player.blockPosition());
            Vec3 endPos = RadiationSource.getPlayerPos(player);

            float radReceived = radSource.emitRadiation(player.level(), startPos, endPos);
            playerRadCap.addRads(radReceived);
            float playerRads = playerRadCap.getRads();
            applyEffects(player, playerRads);

            NuclearWinter.LOGGER.debug("Player Radiation {} for {}. Total Rads: {}", radReceived, player.getName(), playerRads);
        }
    }

    private void applyEffects(Player player, float playerRads) {
        if (player.isCreative()){
            return;
        }

        if (playerRads > RadiationConfig.RADIATION4_RAD_LEVEL) {
            applyRadiationLevelEffect(player, RadiationLevel.EXTREME);
        } else if (playerRads > RadiationConfig.RADIATION3_RAD_LEVEL) {
            applyRadiationLevelEffect(player, RadiationLevel.HIGH);
        } else if (playerRads > RadiationConfig.RADIATION2_RAD_LEVEL) {
            applyRadiationLevelEffect(player, RadiationLevel.MEDIUM);
        } else if (playerRads > RadiationConfig.RADIATION1_RAD_LEVEL) {
            applyRadiationLevelEffect(player, RadiationLevel.LOW);
        }
    }


    // Low - Mild effects(Unable to heal)
    //Medium - Hunger, Mild slowdown
    //High - Health Drop, more severe slowdown, confusion?
    //Extreme - Blindness, health loss to kill, Severe slowdown, near death
    private void applyRadiationLevelEffect(Player player, RadiationLevel radiationLevel) {
        switch(radiationLevel) {
            case EXTREME:
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2));
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 2));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
                return;
            case HIGH:
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100,   1));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
                return;
            case MEDIUM:
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 1));
                return;
            case LOW:
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0));
                return;
            case NONE:
            default:
                break;
        }
    }

    public void doStageTick(Level levelIn){
//        if(getChunkProcessor().isActive()) {
//            NuclearWinter.logger.info("Chunk processor");
//            getChunkProcessor().processChunks(worldIn);
//        }
//        if(levelIn instanceof ServerLevel serverLevel){
//            serverLevel.isRaining()
//            serverLevel.setDayTime(18000);
//            serverLevel.setWeatherParameters(0,6000, true, true);
//        }
        this.tickTime = levelIn.getGameTime();

        NuclearWinter.LOGGER.trace("Stage Tick {} for {}", name, dimKey.toString());
    }

    public String getName(){ return this.name;}

    public StageController.STAGES getStageType() {
        return stageType;
    }

    public boolean canDoNextStage(Level levelIn) {
        if (isNextStageTicksSet() && getTicksLeftTillNextStage(levelIn) <= 0) {
            NuclearWinter.LOGGER.trace("Next stage tick count hit for {}", this.getDimKey().toString());
            return true;
        }

        return false;
    }

    private boolean isNextStageTicksSet() {
        return settings.getNextStageTicks() != 0;
    }

    public long getTicksLeftTillNextStage(Level levelIn) {
        if (settings.getNextStageTicks() == 0) {
            return 0;
        }

        return  (settings.getNextStageTicks() - getTicksInStage(levelIn));
    }

    public long getTicksInStage(Level levelIn) {
        return levelIn.getGameTime() - this.getWorldTickStart();
    }

    public StageSettings getSettings() {
        return settings;
    }

    public long getNextTick() {
        return getTickTime() + 20; //Return last tick + 1 second
    }

    public long getTickTime() {
        return tickTime;
    }


    /**
     * Finalizes the stage. This method is intended to be called when the stage is ending.
     *
     * @param level the level in which the stage is finalized
     */
    public void finalizeStage(Level level) {
        NuclearWinter.LOGGER.info("Finalizing stage " + name);
    }

    /**
     * Initializes the stage. This method is intended to be called when the stage is starting.
     *
     * @param level the level in which the stage is initialized
     */
    public void initStage(Level level) {
        NuclearWinter.LOGGER.info("Initializing stage " + name);
    }

    /**
     * Unloads the stage. This method is intended to be called when the stage is being unloaded(level unload).
     * This method should be used to clean up any resources that were used during the stage.
     * This is different from {@link #finalizeStage(Level)} which is called when the stage is ending.
     *
     * @param level the level in which the stage is unloaded
     */
    public void unloadStage(Level level) {
        NuclearWinter.LOGGER.info("Unloading stage " + name);
    }

//    public RadChunkProcessor getChunkProcessor() {
//        return chunkProcessor;
//    }
//
//    public void setChunkProcessor(RadChunkProcessor chunkProcessor) {
//        this.chunkProcessor = chunkProcessor;
//    }
//
//    public StageBase setChunkProcessorActive(boolean isActive) {
//        this.getChunkProcessor().setActive(isActive);
//        return this;
//    }
}