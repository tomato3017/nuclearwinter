package com.tomatodude.nuclearwinter.radiation;

import com.tomatodude.nuclearwinter.util.RadiationConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RadiationController {
    public static final DamageSource RADIATION_DMG = new DamageSource("radiation").setDamageBypassesArmor();

    public static float getRadResisted(float resistance, float currentRadiation){
        float radDecreaseModifier = resistance/128*0.5f; //Radiation will be decreased by this amount.

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
            if(currentRadLevel < RadiationConfig.PLAYER_NATURAL_RESISTANCE){ //No need to continue if below 65
                return 0;
            }
        }

        return currentRadLevel;

    }

    //Emit radiation from the sky, effects players and world
    public static float emitRadiationFromSky(World worldIn, Vec3d targetPos){
        RadiationSettings radSetting = new RadiationSettings();
        radSetting.setDegradeBlocks(true);
        radSetting.setPlayerEffected(false);
        radSetting.setBlockLightDegradation(false);

        Vec3d startPos = new Vec3d(targetPos.x, RadiationConfig.SKY_RADIATION_START, targetPos.z);

        return emitRadiation(worldIn, startPos, targetPos, radSetting);
    }

    //Emit radiation from a specific block, effects players, not world
    public static float emitRadiationFromBlock(World worldIn, Vec3d startPos, Vec3d endPos){
        RadiationSettings radSetting = new RadiationSettings();
        radSetting.setDegradeBlocks(false);
        radSetting.setPlayerEffected(true);
        radSetting.setBlockLightDegradation(false);

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

    public static float getRadiationResistedByPlayer(EntityPlayer player, float currentRadiation, boolean damageArmor){
        float radiationResisted = 0;
        float radiationLeft = currentRadiation;

        //Pull all the player's armor slots
        Iterable<ItemStack> armorSet = player.getArmorInventoryList();
        radiationResisted = getRadiationResistedByArmor(armorSet, currentRadiation, damageArmor);

        radiationLeft = radiationLeft - RadiationConfig.PLAYER_NATURAL_RESISTANCE;
        radiationResisted = radiationResisted + RadiationConfig.PLAYER_NATURAL_RESISTANCE;

        if(radiationLeft <= 0){
            return currentRadiation;
        }

        return radiationResisted;
    }

    public static float getRadiationResistedByArmor(Iterable<ItemStack> armorSet, float currentRadiation, boolean damageArmor){
        float radiationResisted = 0;
        float radiationLeft = currentRadiation;

        for (ItemStack armor : armorSet) {
            if(armor.getItem() instanceof IRadResistent) {
                IRadResistent radArmor = (IRadResistent) armor.getItem();
                float resistedByArmor = getRadResisted(radArmor.getRadResistance(), radiationLeft);
                if (damageArmor && radArmor.canRadDamageArmor()) {
                    //TODO: Either implement this or remove it.
                    //damageArmorFromRads(armor, radiationLeft);
                }
                radiationResisted = radiationResisted + resistedByArmor;
                radiationLeft = radiationLeft - resistedByArmor;
                if (radiationLeft <= 0) {
                    return currentRadiation;
                }
            }
        }

        return radiationResisted;
    }

    //Returns the radiation left over after the player has resistances calculated.
    public static float getRadiationAbsorbedByPlayer(EntityPlayer player, float currentRadiation, boolean damageArmor){
        return Math.max(currentRadiation - getRadiationResistedByPlayer(player,currentRadiation,damageArmor), 0.0f);
    }
    private static float getDegradedBlockResistance(float resistance, float lightDegradeFactor){
        return resistance-(resistance*lightDegradeFactor);
    }

    private static float getCurrentRadLevel() {
        return RadiationConfig.MAX_RADIATION_LEVEL;
    }

}
