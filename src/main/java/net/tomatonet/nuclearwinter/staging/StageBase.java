package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tomatonet.nuclearwinter.NuclearWinter;
import net.tomatonet.nuclearwinter.capabilities.CapabiltiesAttacher;
import net.tomatonet.nuclearwinter.radiation.IRadiationReceiver;
import net.tomatonet.nuclearwinter.radiation.RadiationConfig;
import net.tomatonet.nuclearwinter.radiation.RadiationSettings;
import net.tomatonet.nuclearwinter.radiation.RadiationSource;

public abstract class StageBase {
    private final StageSettings settings;
    private final String name;
    private final ResourceLocation dimKey;
    private final StageController.STAGES stageType;
    private final long worldTickStart;

    private long tickTime; //The time of the last tick
    private long ticksTillNextStage = 0;

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
            if (playerRads > RadiationConfig.MAX_RADIATION_LEVEL){
                player.kill(); //TODO replace with radiation effect that kills quickly
            }

            NuclearWinter.LOGGER.debug("Player Radiation {} for {}. Total Rads: {}", radReceived, player.getName(), playerRads);
        }
    }

    public void doStageTick(Level levelIn){
//        if(getChunkProcessor().isActive()) {
//            NuclearWinter.logger.info("Chunk processor");
//            getChunkProcessor().processChunks(worldIn);
//        }

        this.tickTime = levelIn.getGameTime();

        NuclearWinter.LOGGER.trace("Stage Tick {} for {}", name, dimKey.toString());
    }

    public String getName(){ return this.name;}

    public StageController.STAGES getStageType() {
        return stageType;
    }
//    public double getMaxRadiation() {
//        return maxRadiation;
//    }
//
//    public StageBase setMaxRadiation(double maxRadiation) {
//        this.maxRadiation = maxRadiation;

//        return this;

//    }
    public boolean canDoNextStage(Level levelIn) {
        if (ticksTillNextStage != 0 && levelIn.getGameTime() - this.getWorldTickStart() >= ticksTillNextStage) {
            NuclearWinter.LOGGER.trace("Next stage tick count hit for {}", this.getDimKey().toString());
            return true;
        }

        return false;
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

    public StageBase withTimeTillNextStage(long ticksTillNextStage) {
        this.ticksTillNextStage = ticksTillNextStage;
        return this;
    }

    public StageBase withDaysTillNextStage(long daysTillNextStage) {
        return this.withTimeTillNextStage(daysTillNextStage * 24000);
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