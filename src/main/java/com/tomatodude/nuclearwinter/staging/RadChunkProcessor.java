package com.tomatodude.nuclearwinter.staging;

import com.tomatodude.nuclearwinter.NuclearWinter;
import com.tomatodude.nuclearwinter.radiation.RadiationController;
import com.tomatodude.nuclearwinter.util.Coord2d;
import com.tomatodude.nuclearwinter.util.RadChunkPos;
import com.tomatodude.nuclearwinter.util.RadiationConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RadChunkProcessor {

    private final int chunkProcChunkPerTick = RadiationConfig.CHUNKPROC_CHUNK_PER_TICK;
    private final int chunkProcBlockPerTick = RadiationConfig.CHUNKPROC_BLOCKSTATIC_PER_TICK;
    private double maxRadiation;
    private boolean isActive = false;
    private ArrayDeque<RadChunkPos> chunkQueue;

    public RadChunkProcessor(double maxRadiation) {
        this.maxRadiation = maxRadiation;

        chunkQueue = new ArrayDeque<>();
    }

    public void processChunks(World worldIn) {
        if(chunkQueue.isEmpty()){
            loadChunksFromWorldToQueue(worldIn);
        }

        int chunksProcessed = 0;
        do {
            RadChunkPos nextChunk = chunkQueue.poll();

            if (nextChunk != null) {
                boolean isLoaded = ((WorldServer) worldIn).getChunkProvider().chunkExists(nextChunk.x, nextChunk.z);

                if (nextChunk != null && isLoaded) {
                    processChunk(worldIn, nextChunk);
                    chunksProcessed++;

                    if (!nextChunk.isNeedsNuking()) {
                        chunkQueue.add(nextChunk); //Add it back to the end
                    }
                }
            } else {
                break;
            }
        } while (chunksProcessed < chunkProcChunkPerTick) ;

    }

    private void processChunk(World worldIn, RadChunkPos chunk) {
        if(!chunk.isNeedsNuking()){
            List<Coord2d> blockCoords = chunk.getNextBlockPosSet();

            for (Coord2d coord : blockCoords){
                //Nuke to bedrock if needed
                BlockPos realPos = chunk.getBlock(coord.getX(),0,coord.getZ());
                RadiationController.emitRadiationFromSky(worldIn,new Vec3d(realPos.getX(),realPos.getY(),realPos.getZ()));
            }
        } else {
            //TODO: Chunk nuking
        }
    }

    private void loadChunksFromWorldToQueue(World worldIn) {
        ChunkProviderServer chunkProv = (ChunkProviderServer) worldIn.getChunkProvider();

        Collection<Chunk> chunkList = chunkProv.getLoadedChunks();
        Iterator<Chunk> iterator = chunkList.iterator();

        while(iterator.hasNext()){
            Chunk c = iterator.next();
            chunkQueue.add(new RadChunkPos(c.x,c.z));
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public RadChunkProcessor setActive(boolean active) {
        isActive = active;
        return this;
    }

    public void AddChunkToProcessor(RadChunkPos c){
        if(!chunkQueue.contains(c)){
            NuclearWinter.logger.debug("Adding chunk to processor " + c.x + "," + c.z);
            chunkQueue.add(c);
        }
    }

    public void RemoveChunkFromProcessor(RadChunkPos c){
        if(chunkQueue.remove(c)){
            NuclearWinter.logger.debug("Removing chunk from processor " + c.x + "," + c.z);
        }
    }



    public double getMaxRadiation() {
        return maxRadiation;
    }

    public void setMaxRadiation(double maxRadiation) {
        this.maxRadiation = maxRadiation;
    }

    public int getChunkProcChunkPerTick() {
        return chunkProcChunkPerTick;
    }

    public int getChunkProcBlockPerTick() {
        return chunkProcBlockPerTick;
    }

}
