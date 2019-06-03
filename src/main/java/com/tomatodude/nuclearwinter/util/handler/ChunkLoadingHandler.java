package com.tomatodude.nuclearwinter.util.handler;

import com.tomatodude.nuclearwinter.staging.RadChunkProcessor;
import com.tomatodude.nuclearwinter.staging.StageController;
import com.tomatodude.nuclearwinter.util.RadChunkPos;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class ChunkLoadingHandler {

    @SubscribeEvent
    public static void ChunkLoad(ChunkEvent.Load e){
        if(!e.getWorld().isRemote && StageController.getLoadedStage(e.getWorld()) != null){
            RadChunkProcessor chunkProc = StageController.getLoadedStage(e.getWorld()).getChunkProcessor();

            if(chunkProc.isActive()){
                chunkProc.AddChunkToProcessor(new RadChunkPos(e.getChunk().getPos()));
            }
        }
    }

    @SubscribeEvent
    public static void ChunkUnload(ChunkEvent.Unload e){
        if(!e.getWorld().isRemote && StageController.getLoadedStage(e.getWorld()) != null){
            RadChunkProcessor chunkProc = StageController.getLoadedStage(e.getWorld()).getChunkProcessor();

            if(chunkProc.isActive()){
                chunkProc.RemoveChunkFromProcessor(new RadChunkPos(e.getChunk().getPos()));
            }
        }
    }
}
