package com.tomatodude.nuclearwinter.staging;

import com.tomatodude.nuclearwinter.NuclearWinter;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

import javax.annotation.Nullable;
import java.io.InvalidClassException;
import java.util.HashMap;

@EventBusSubscriber
public class StageController {

    public enum STAGES{
        INITIAL(0),APOCLOW(1),APOCMED(2),APOCHIGH(3),POSTAPOC(4);

        private final int value;
        private final static HashMap<Integer, STAGES> map = new HashMap<>();

        static {
            for (STAGES stage : STAGES.values()){
                map.put(stage.value, stage);
            }
        }

        STAGES(int i) {
            this.value = i;
        }

        public static STAGES valueOf(int stageNum){
            return map.get(stageNum);
        }

        public int getValue() {
            return value;
        }

    }

    public static HashMap<Integer, StageBase> activeStageMap = new HashMap<>();

    protected static IStageWorldSettings getStageWorldSettings(World worldIn) {
        return worldIn.getCapability(StageWorldSettingsProvider.STAGE_WORLD_SETTINGS_CAPABILITY, null);
    }

    protected static boolean hasStageWorldSettings(World world) {
        return world.hasCapability(StageWorldSettingsProvider.STAGE_WORLD_SETTINGS_CAPABILITY, null);
    }

    public static StageBase activateStaging(World world, @Nullable STAGES stageType) {
        if(!world.isRemote && hasStageWorldSettings(world)){
            NuclearWinter.logger.info("Activating staging for " + world.provider.getDimension()); //TODO: Set to debug
            if(stageType == null) {
                stageType = STAGES.INITIAL;
            }
            IStageWorldSettings stageWorldSettings = world.getCapability(StageWorldSettingsProvider.STAGE_WORLD_SETTINGS_CAPABILITY, null);
            stageWorldSettings.setCurrentStage(stageType);
            stageWorldSettings.setStartWorldTime(world.getTotalWorldTime());
            stageWorldSettings.setActive(true);
            return loadStage(world,stageWorldSettings); //Just to make sure
        }

        return null;
    }

    public static boolean setToNextStage(World world, IStageWorldSettings stageWorldSettings) {
        StageBase stage;
        if(hasNextStage(stageWorldSettings)){
            STAGES stageType = getNextStage(stageWorldSettings);
            activateStaging(world, stageType);

            return true;
        } else {
            return false;
        }
    }

    public static StageBase getLoadedStage(World world) {
        return activeStageMap.get(world.provider.getDimension());
    }

    private static boolean hasNextStage(IStageWorldSettings stageWorldSettings) {
        return STAGES.valueOf(stageWorldSettings.getCurrentStage().getValue()+1) != null;
    }

    private static STAGES getNextStage(IStageWorldSettings stageWorldSettings) {
        int currentStage = stageWorldSettings.getCurrentStage().getValue();
        return STAGES.valueOf(currentStage + 1);
    }

    private static StageBase getCurrentStageFromWorld(World world, IStageWorldSettings stageWorldSettings) throws InvalidClassException {
        int dimID = world.provider.getDimension(); //Get the dimension
        long worldTickStart = stageWorldSettings.getStartWorldTime();

        switch (stageWorldSettings.getCurrentStage()){
            case INITIAL:
                return new StagePreapoc(dimID, worldTickStart);
            case APOCLOW:
                return new StageApocLow(dimID, worldTickStart);
            case APOCMED:
                return new StageApocMed(dimID, worldTickStart);
            case APOCHIGH:
                return new StageApocHigh(dimID, worldTickStart);
            case POSTAPOC:
                return new StagePostApoc(dimID, worldTickStart);
            default:
                throw new InvalidClassException("Unable to locate class " + stageWorldSettings.getCurrentStage().toString());
        }
    }

    @SubscribeEvent
    public static void onWorldTick(WorldTickEvent event){
        if(event.phase == TickEvent.Phase.START && !event.world.isRemote){
            World world = event.world;
            if(hasStageWorldSettings(world)){
                IStageWorldSettings stageWorldSettings = getStageWorldSettings(world);
                if(!stageWorldSettings.isActive()){ return; } //Only run if the world is active

                StageBase stage = getLoadedStage(world);
                if(stage.getNextTick() <= world.getTotalWorldTime()){
                    stage.doStageTick(world);
                    stage.setTickTime(world.getTotalWorldTime());
                }

                if(stage.canDoNextStage(world)){
                    stage.finalizeStage(world);

                    if(!setToNextStage(world, stageWorldSettings)){
                        stageWorldSettings.setActive(false);
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event){
        World world = event.getWorld();
        if(!world.isRemote && hasStageWorldSettings(world)){
            IStageWorldSettings stageWorldSettings = world.getCapability(StageWorldSettingsProvider.STAGE_WORLD_SETTINGS_CAPABILITY, null);
            NuclearWinter.logger.info("Current stage is "  + stageWorldSettings.getCurrentStage().toString() + " " + world.provider.getDimension()); //TODO: Remove

            //Get the stage we need to load
            //DIMID, world tick start
            loadStage(world, stageWorldSettings);

        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event){
        World world = event.getWorld();
        if(!world.isRemote && hasStageWorldSettings(world)){
            IStageWorldSettings stageWorldSettings = world.getCapability(StageWorldSettingsProvider.STAGE_WORLD_SETTINGS_CAPABILITY, null);

            //Get the stage we need to load
            //DIMID, world tick start
            unloadStage(world, stageWorldSettings);

        }
    }

    private static StageBase loadStage(World world, IStageWorldSettings stageWorldSettings) {
        StageBase stage;
        try {
            stage = getCurrentStageFromWorld(world, stageWorldSettings);
        } catch (InvalidClassException e) {
            e.printStackTrace();
            return null;
        }

        activeStageMap.put(stage.getDimID(),stage);
        stage.initStage(world);
        return stage;
    }

    private static void unloadStage(World world, IStageWorldSettings stageWorldSettings) {
            StageBase stage = getLoadedStage(world);
            stage.unloadStage(world);
            activeStageMap.remove(stage.getDimID());
    }

}
