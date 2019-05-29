package com.tomatodude.nuclearwinter.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RadChunkPos extends ChunkPos {
    private static final Random random = new Random();
    private boolean needsNuking = false;
    private int currentBlockPos = 0;

    public RadChunkPos(int x, int z) {
        super(x, z);
    }

    public RadChunkPos(BlockPos pos) {
        super(pos);
    }

    public boolean isNeedsNuking() {
        return needsNuking;
    }

    public RadChunkPos setNeedsNuking(boolean needsNuking) {
        this.needsNuking = needsNuking;
        return this;
    }

    public List<Coord2d> getNextBlockPosSet(){
        List<Coord2d> blockPosList = new ArrayList<>(RadiationConfig.CHUNKPROC_BLOCKSTATIC_PER_TICK +
                                                        RadiationConfig.CHUNKPROC_BLOCKRANDOM_PER_TICK);

        //First get the next set of blocks that need to be worked
        for(int i=0; i<RadiationConfig.CHUNKPROC_BLOCKSTATIC_PER_TICK; i++){
            blockPosList.add(getCoordPosFromInt(currentBlockPos));
            currentBlockPos++;
            if(currentBlockPos > 255){
                currentBlockPos = 0;
            }
        }

        //Next fill in some randomness
        for(int i=0; i<RadiationConfig.CHUNKPROC_BLOCKRANDOM_PER_TICK; i++){
            int nextValue = random.nextInt(256);
            blockPosList.add(new Coord2d(nextValue));
        }


        return blockPosList;
    }

    //x=lower 4 bits, z= upper 4
    private Coord2d getCoordPosFromInt(int currentBlockPos) {
        return new Coord2d(currentBlockPos);
    }


}
