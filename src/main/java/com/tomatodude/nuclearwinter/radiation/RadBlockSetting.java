package com.tomatodude.nuclearwinter.radiation;

import com.tomatodude.nuclearwinter.util.RadiationConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//TODO: Work on this
public class RadBlockSetting {
    private String blockName; //Block name
    private float blockResistance; //Block resistance 0-255

    private String degradedBlockName; //Degraded Block name
    private float blockDegradationLevel; //When a block receives this much radiation at once, it degrades

    public static RadBlockSetting getResistanceOfBlock(IBlockState blockState, World world, BlockPos currentBlockPos) {
        Block block = blockState.getBlock();
        ResourceLocation blockName = block.getRegistryName();
        RadBlockSetting radMapped = RadiationConfig.getMapping(blockName.toString());

        if(radMapped != null){ //First check to see if the resistance is a config mapped entry
            return radMapped;
        } else if(block instanceof IRadResistent){ //next try the interface
            return new RadBlockSetting(blockName.toString(),((IRadResistent) block).getRadResistance());
        } else { //Lastly, try the block's hardness for a resist.
            float blockHardness = blockState.getBlockHardness(world,currentBlockPos);
            return new RadBlockSetting(blockName.toString(), getResistanceFromHardness(blockHardness));
        }
    }

    private static float getResistanceFromHardness(float blockHardness) {
        if (blockHardness < 0.5f) { //Less then dirt
            return 0.0f;
        } else if (blockHardness < 1.5f) { //Less then stone
            return RadiationConfig.BLOCK_RESIST_HARD_LOW;
        } else if (blockHardness < 5.1f) { //Just above metal blocks
            return RadiationConfig.BLOCK_RESIST_HARD_MED;
        } else if (blockHardness >= 5.1f) { //Obsidian, etc
            return RadiationConfig.BLOCK_RESIST_HARD_HIGH;
        }

        return 0.0f;
    }

    public float getBlockResistance(){
        return blockResistance;
    }

    public RadBlockSetting(String blockName, float blockResistance, String degradedBlockName, float blockDegradationLevel) {
        this.blockDegradationLevel = blockDegradationLevel;
        this.blockResistance = blockResistance;
        this.blockName = blockName;
        this.degradedBlockName = degradedBlockName;
    }

    public RadBlockSetting(String blockName, float blockResistance) {
        this.blockResistance = blockResistance;
        this.blockName = blockName;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getDegradedBlockName() {
        return degradedBlockName;
    }

    public boolean isBlockDegrade() {

        if (getBlockDegradationLevel() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public float getBlockDegradationLevel() {
        return blockDegradationLevel;
    }

    public IBlockState getBlockDegradedState() {
        if(degradedBlockName == null){
            return null;
        }
        return Block.REGISTRY.getObject(new ResourceLocation(degradedBlockName)).getDefaultState();
    }
}
