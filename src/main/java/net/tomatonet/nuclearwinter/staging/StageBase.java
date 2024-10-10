package net.tomatonet.nuclearwinter.staging;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.tomatonet.nuclearwinter.NuclearWinter;

public abstract class StageBase {
    private String name;
    private ResourceLocation dimKey;
    private long tickTime; //The time of the last tick
    private StageController.STAGES stageType;
    private long worldTickStart;
    private long ticksTillNextStage = 0;

    //private double maxRadiation = RadiationConfig.MAX_RADIATION_LEVEL_PREAPOC;

    //private RadChunkProcessor chunkProcessor = new RadChunkProcessor(maxRadiation);

    public StageBase(String name, ResourceLocation dimKey, long worldTickStart, StageController.STAGES stageType) {
        this.name = name;
        this.dimKey = dimKey;
        this.worldTickStart = worldTickStart;
        this.stageType = stageType;
    }

    public ResourceLocation getDimKey() {
        return dimKey;
    }

    public long getWorldTickStart() {
        return worldTickStart;
    }

    public StageBase setWorldTickStart(long worldTickStart) {
        this.worldTickStart = worldTickStart;
        return this;
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