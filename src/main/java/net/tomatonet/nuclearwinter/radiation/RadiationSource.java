package net.tomatonet.nuclearwinter.radiation;

//rad level
//target position
//emit rads
    //Target player?

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tomatonet.nuclearwinter.NuclearWinter;
import org.slf4j.Logger;

public class RadiationSource {
    private RadiationSettings radSettings;

    public RadiationSource(RadiationSettings radSettings) {
        this.radSettings = radSettings;
    }

    public static float getBlockLightDegradeFactor(Level level, Vec3 endPos) {
        float blockLightDegradeFactor=0.0f;
        //Get the lighting at the end position, we'll use that to see if we should degrade the resistance factor
        BlockPos lightBlockPos = new BlockPos((int)endPos.x, (int) endPos.y, (int) endPos.z);
        int lightLevel = level.getBrightness(LightLayer.SKY, lightBlockPos);
        if(lightLevel > RadiationConfig.RAD_MAX_LIGHT_LEVEL){
            blockLightDegradeFactor = (1.0f/RadiationConfig.RAD_BLOCK_LIGHT_DEGRADE_FACTOR)*
                    (lightLevel-RadiationConfig.RAD_MAX_LIGHT_LEVEL);
        }
        return blockLightDegradeFactor;
    }

    private static float getDegradedBlockResistance(float resistance, float lightDegradeFactor){
        return resistance-(resistance*lightDegradeFactor);
    }

    public static float getRadResisted(float resistance, float currentRadiation){
        float radDecreaseModifier = resistance/128*0.5f; //Radiation will be decreased by this amount.

        return currentRadiation*radDecreaseModifier;
    }

    public static Vec3 getSkyPos(Level level, BlockPos blockPos) {
        return new Vec3(blockPos.getX(), level.getMaxBuildHeight(), blockPos.getZ());
    }

    //Emit radiation
    public float emitRadiation(Level level, Vec3 startPos, Vec3 endPos) {
        Logger logger = NuclearWinter.LOGGER;
        //This is the factor we will reduce the resistance by.
        float blockLightDegradeFactor = getBlockLightDegradeFactor(level, endPos);
        //Ok first lets get the vector that the radiation is going to travel on from the startPos
        Vec3 travelVector = endPos.subtract(startPos);
        //Norm so we have our direction and a scalar to manipulate
        Vec3 travelVectorNorm = travelVector.normalize();
        double length = travelVector.length();
        float currentRadLevel = radSettings.getStartRadLevel();
        //Vector we use in the for loop
        Vec3 currentVector = travelVectorNorm;

        //TODO remove this
        logger.trace("Emitting radiation from " + startPos + " to " +
                endPos + " with a length of " + length + " and a rad level of " + currentRadLevel);
        logger.trace("Block light degrade factor: " + blockLightDegradeFactor);

        for (double currentPos = 0; currentPos < length; currentPos++){
            double currentX, currentY, currentZ;
            currentX = startPos.x + currentVector.x;
            currentY = startPos.y + currentVector.y;
            currentZ = startPos.z + currentVector.z;

            BlockPos currentBlockPos = new BlockPos((int) currentX, (int) currentY, (int) currentZ);
            BlockState blockState = level.getBlockState(currentBlockPos);

            if(blockState.getBlock() != Blocks.AIR){ //TODO: Optimize this by getting the heightmap first
                BlockState testBlockState = Blocks.STONE.defaultBlockState();
                level.setBlock(currentBlockPos,testBlockState,Block.UPDATE_NEIGHBORS | Block.UPDATE_CLIENTS);
                RadBlockSetting radResist = RadBlockRegistry.getResistanceOfBlock(blockState, level, currentBlockPos);
                float degradedBlockResistance = getDegradedBlockResistance(radResist.getBlockResistance(),blockLightDegradeFactor);
                float radiationResisted = getRadResisted(radResist.getBlockResistance(), currentRadLevel);

//                //Check if the block degrades, if it does see if the radiation resisted is higher then where it degrades
//                if(settings.isDegradeBlocks() && radResist.isBlockDegrade() &&
//                        radiationResisted > radResist.getBlockDegradationLevel()){
//                    IBlockState degradedBlockState = radResist.getBlockDegradedState();
//                    world.setBlockState(currentBlockPos,degradedBlockState,3);
//                }

                //Recalc for lighting post block degradation
                if(radSettings.isPlayerEffected() || radSettings.isBlockLightDegradation()) {
                    radiationResisted = getRadResisted(degradedBlockResistance, currentRadLevel);
                }

                currentRadLevel = currentRadLevel - Math.max(radiationResisted,0.0f);
            }

            currentVector = currentVector.add(travelVectorNorm);
            if(currentRadLevel < RadiationConfig.PLAYER_NATURAL_RESISTANCE){ //No need to continue if below 65
                return 0;
            }
        }

        return currentRadLevel;
    }
}
