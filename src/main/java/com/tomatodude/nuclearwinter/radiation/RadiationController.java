package com.tomatodude.nuclearwinter.radiation;

import com.tomatodude.nuclearwinter.NuclearWinter;
import com.tomatodude.nuclearwinter.util.RadiationConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

//TODO: Armor rad resistance
public class RadiationController {
    public static final DamageSource RADIATION_DMG = new DamageSource("radiation").setDamageBypassesArmor();

    public static float getRadResisted(float blockResistance, float currentRadiation){
        float radDecreaseModifier = blockResistance/128*0.5f; //Radiation will be decreased by this amount.

        return currentRadiation*radDecreaseModifier;
    }

    //Emits radiation given a source position
    public static float emitRadiation(World world, Vec3d startPos, Vec3d endPos, RadiationSettings settings){
        //This is the factor we will reduce the resistance by.
        float blockLightDegradeFactor = getBlockLightDegradeFactor(world, endPos);
        //Ok first lets get the vector that the radiation is going to travel on from the startPos
        Vec3d travelVector = endPos.subtract(startPos);
        //Norm so we have our direction and a scalar to manipulate
        Vec3d travelVectorNorm = travelVector.normalize();
        //get the total distance were traveling
        double length = travelVector.lengthVector();
        float currentRadLevel = settings.getStartRadLevel();
        //Vector we use in the for loop
        Vec3d currentVector = travelVectorNorm;

        for (double currentPos = 0; currentPos < length; currentPos++){
            double currentX, currentY, currentZ;
            currentX = startPos.x + currentVector.x;
            currentY = startPos.y + currentVector.y;
            currentZ = startPos.z + currentVector.z;

            BlockPos currentBlockPos = new BlockPos(currentX, currentY, currentZ);
            IBlockState blockState = world.getBlockState(currentBlockPos);

            if(blockState.getBlock() != Blocks.AIR){
                RadBlockSetting radResist = RadBlockSetting.getResistanceOfBlock(blockState, world, currentBlockPos);
                float degradedBlockResistance = getDegradedBlockResistance(radResist.getBlockResistance(),blockLightDegradeFactor);
                //TODO: Remove
                NuclearWinter.logger.debug(radResist.getBlockName() + " Resistance: " + degradedBlockResistance);
                float radiationResisted = getRadResisted(radResist.getBlockResistance(), currentRadLevel);

                //Check if the block degrades, if it does see if the radiation resisted is higher then where it degrades
                if(settings.isDegradeBlocks() && radResist.isBlockDegrade() &&
                        radiationResisted > radResist.getBlockDegradationLevel()){
                    IBlockState degradedBlockState = radResist.getBlockDegradedState();
                    world.setBlockState(currentBlockPos,degradedBlockState,3);
                }

                //Recalc for lighting post block degradation
                if(settings.isPlayerEffected() || settings.isBlockLightDegradation()) {
                    radiationResisted = getRadResisted(degradedBlockResistance, currentRadLevel);
                }

                currentRadLevel = currentRadLevel - radiationResisted;
            }

            currentVector = currentVector.add(travelVectorNorm);

        }

        return currentRadLevel;

    }

    //Emit radiation from the sky, effects players and world
    public static float emitRadiationFromSky(World worldIn, Vec3d targetPos){
        RadiationSettings radSetting = new RadiationSettings();
        radSetting.setDegradeBlocks(true);
        radSetting.setPlayerEffected(true);

        Vec3d startPos = new Vec3d(targetPos.x, RadiationConfig.SKY_RADIATION_START, targetPos.z);

        return emitRadiation(worldIn, startPos, targetPos, radSetting);
    }

    //Emit radiation from a specific block, effects players, not world
    public static float emitRadiationFromBlock(World worldIn, Vec3d startPos, Vec3d endPos){
        RadiationSettings radSetting = new RadiationSettings();
        radSetting.setDegradeBlocks(false);
        radSetting.setPlayerEffected(true);

        return emitRadiation(worldIn, startPos, endPos, radSetting);
    }

    public static float getBlockLightDegradeFactor(World world, Vec3d endPos) {
        float blockLightDegradeFactor=0.0f;
        //Get the lighting at the end position, we'll use that to see if we should degrade the resistance factor
        BlockPos lightBlockPos = new BlockPos((int)endPos.x, (int) endPos.y, (int) endPos.z);
        Chunk chunk = world.getChunkFromBlockCoords(lightBlockPos);
        int lightLevel = chunk.getLightFor(EnumSkyBlock.SKY, lightBlockPos);
        if(lightLevel > RadiationConfig.RAD_MAX_LIGHT_LEVEL){
            blockLightDegradeFactor = (1.0f/RadiationConfig.RAD_BLOCK_LIGHT_DEGRADE_FACTOR)*(lightLevel-RadiationConfig.RAD_MAX_LIGHT_LEVEL);
        }
        return blockLightDegradeFactor;
    }

    private static float getDegradedBlockResistance(float resistance, float lightDegradeFactor){
        return resistance-(resistance*lightDegradeFactor);
    }

    private static float getCurrentRadLevel() {
        return RadiationConfig.MAX_RADIATION_LEVEL;
    }
}
