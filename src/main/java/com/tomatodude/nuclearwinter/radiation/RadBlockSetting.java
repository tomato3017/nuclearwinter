package com.tomatodude.nuclearwinter.radiation;

import com.tomatodude.nuclearwinter.NuclearWinter;
import com.tomatodude.nuclearwinter.util.RadiationConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.security.InvalidParameterException;

public class RadBlockSetting {
    public static int BLOCKSTATE_DEFAULT_ID = -1;

    private String blockName; //Block name

    private int blockMetaID; //Block Meta ID
    private float blockResistance; //Block resistance 0-255

    private String degradedBlockName; //Degraded Block name

    private int degradedBlockMetaID; //Degraded Block Meta ID
    private float blockDegradationLevel; //When a block receives this much radiation at once, it degrades

    public static RadBlockSetting getResistanceOfBlock(IBlockState blockState, World world, BlockPos currentBlockPos) {
        Block block = blockState.getBlock();
        ResourceLocation blockName = block.getRegistryName();
        RadBlockSetting radMapped = RadiationConfig.getMapping(blockName.toString(), block.getMetaFromState(blockState));

        if (radMapped != null) { //First check to see if the resistance is a config mapped entry
            return radMapped;
        } else if (block instanceof IRadResistent) { //next try the interface
            return new RadBlockSetting(blockName.toString(), ((IRadResistent) block).getRadResistance());
        } else { //Lastly, try the block's hardness for a resist.
            float blockHardness = blockState.getBlockHardness(world, currentBlockPos);
            return new RadBlockSetting(blockName.toString(), getResistanceFromHardness(blockHardness));
        }
    }

    //As a fallback we will use the resistance of the block as a fallback
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

    public int getDegradedBlockMetaID() {
        return degradedBlockMetaID;
    }

    public float getBlockResistance() {
        return blockResistance;
    }

    public RadBlockSetting(String blockName, float blockResistance) {
        this.blockDegradationLevel = 0;
        this.blockResistance = blockResistance;
        this.blockName = blockName;
        this.blockMetaID = BLOCKSTATE_DEFAULT_ID;
    }

    protected int getBlockMetaID(String blockResourceString) {
        String[] blockResArr = blockResourceString.split(":");
        if (blockResArr.length > 2) {
            String testVal = blockResArr[blockResArr.length - 1];

            try {
                return Integer.parseInt(testVal);
            } catch (NumberFormatException e) {
                NuclearWinter.logger.error("Unable to parse block state id from Block String ID");
                throw new InvalidParameterException("Unable to parse block state id from Block String ID");
            }
        } else {
            // -1 means we will use the default state of the block
            return BLOCKSTATE_DEFAULT_ID;
        }
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
        if (degradedBlockName == null) {
            return null;
        }

        if(isDefaultDegradedBlockState()) {
            return Block.REGISTRY.getObject(new ResourceLocation(degradedBlockName)).getDefaultState();
        }

        Block degradedBlock = Block.REGISTRY.getObject(new ResourceLocation(degradedBlockName));
        return degradedBlock.getStateFromMeta(getDegradedBlockMetaID());
    }


    public int getBlockMetaID() {
        return blockMetaID;
    }

    public RadBlockSetting setBlockMetaID(int blockMetaID) {
        //TODO Validate the meta id
        this.blockMetaID = blockMetaID;
        return this;
    }

    public RadBlockSetting setBlockResistance(float blockResistance) {
        this.blockResistance = blockResistance;
        return this;
    }

    public RadBlockSetting setDegradedBlockName(String degradedBlockName) {
        this.degradedBlockName = degradedBlockName;
        return this;
    }

    public RadBlockSetting setDegradedBlockMetaID(int degradedBlockMetaID) {
        this.degradedBlockMetaID = degradedBlockMetaID;
        return this;
    }

    public RadBlockSetting setBlockDegradationLevel(float blockDegradationLevel) {
        this.blockDegradationLevel = blockDegradationLevel;
        return this;
    }

    public String getFullBlockResourceName(){
        return this.blockName + ":" + this.blockMetaID;
    }

    public String getFullDegradedBlockResourceName(){
        return this.degradedBlockName + ":" + this.degradedBlockMetaID;
    }

    public boolean isDefaultBlockState(){
        return this.blockMetaID == BLOCKSTATE_DEFAULT_ID;
    }

    public boolean isDefaultDegradedBlockState(){
        return this.degradedBlockMetaID == BLOCKSTATE_DEFAULT_ID;
    }
}