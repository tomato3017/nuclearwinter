package com.tomatodude.nuclearwinter.staging;

import com.tomatodude.nuclearwinter.NuclearWinter;
import com.tomatodude.nuclearwinter.util.RadiationConfig;
import net.minecraft.world.World;

public abstract class StageBase {

    private String name;
    private int dimID;
    private long tickTime;
    private double maxRadiation = RadiationConfig.MAX_RADIATION_LEVEL_PREAPOC;
    private RadChunkProcessor chunkProcessor = new RadChunkProcessor(maxRadiation);

    public int getDimID() {
        return dimID;
    }

    public StageBase setDimID(int dimID) {
        this.dimID = dimID;
        return this;
    }

    public long getWorldTickStart() {
        return worldTickStart;
    }

    public StageBase setWorldTickStart(long worldTickStart) {
        this.worldTickStart = worldTickStart;
        return this;
    }

    public void doStageTick(World worldIn){
        if(getChunkProcessor().isActive()) {
            NuclearWinter.logger.info("Chunk processor");
            getChunkProcessor().processChunks(worldIn);
        }

        NuclearWinter.logger.info("Stage Tick " + this.getName());

        return;
    }

    private long worldTickStart;

    public String getName(){ return this.name;}

    public double getMaxRadiation() {
        return maxRadiation;
    }

    public StageBase setMaxRadiation(double maxRadiation) {
        this.maxRadiation = maxRadiation;
        return this;
    }

    public abstract boolean canDoNextStage(World worldIn);

    public StageBase(String name, int dimID, long worldTickStart) {
        this.name = name;
        this.dimID = dimID;
        this.worldTickStart = worldTickStart;
    }

    public long getNextTick() {
        return getTickTime() + 20; //Return last tick + 1 second
    }

    public long getTickTime() {
        return tickTime;
    }

    public StageBase setTickTime(long tickTime) {
        this.tickTime = tickTime;
        return this;
    }

    public void finalizeStage(World world) {

    }

    public void initStage(World world) {

    }

    public void unloadStage(World world) {
        return;
    }

    public RadChunkProcessor getChunkProcessor() {
        return chunkProcessor;
    }

    public void setChunkProcessor(RadChunkProcessor chunkProcessor) {
        this.chunkProcessor = chunkProcessor;
    }

    public StageBase setChunkProcessorActive(boolean isActive) {
        this.getChunkProcessor().setActive(isActive);
        return this;
    }
}
