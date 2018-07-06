package com.tomatodude.nuclearwinter.staging;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

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

    @SubscribeEvent
    public static void onWorldTick(WorldTickEvent event){
        if(event.phase == TickEvent.Phase.START && !event.world.isRemote){

        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event){
        World world = event.getWorld();
        if(!world.isRemote && world.hasCapability(StageWorldSettingsProvider.STAGE_WORLD_SETTINGS_CAPABILITY,null)){

        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Unload event){

    }

}
