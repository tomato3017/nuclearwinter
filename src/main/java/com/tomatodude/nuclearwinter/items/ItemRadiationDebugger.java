package com.tomatodude.nuclearwinter.items;

import com.tomatodude.nuclearwinter.staging.StageController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemRadiationDebugger extends ItemBase {
    private static final String ITEM_NAME = "Radiation_Debugger";

    public ItemRadiationDebugger() {
        super(ITEM_NAME);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
            ItemStack itemStackIn = playerIn.getHeldItem(handIn);
            if(!worldIn.isRemote) {


//                RadiationSettings radSettings = new RadiationSettings();
//                radSettings.setPlayerEffected(true);
//
//                //float test = RadiationController.emitRadiationFromSky(worldIn, playerIn.getPositionVector());
//                float test = RadiationController.emitRadiation(worldIn, new Vec3d(playerIn.posX, 255, playerIn.posZ), playerIn.getPositionVector(), radSettings);
//
//
//
//                BlockPos bp = new BlockPos((int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
//                playerIn.sendMessage(new TextComponentString("Current Rad level is " + test));
//                playerIn.sendMessage(new TextComponentString(String.valueOf(worldIn.getChunkFromBlockCoords(bp).getLightFor(EnumSkyBlock.SKY, bp))));
//                if(playerIn.isPotionActive(RegistryHandler.radiation)){
//                    PotionEffect PE = playerIn.getActivePotionEffect(RegistryHandler.radiation);
//                    playerIn.addPotionEffect(new PotionEffect(RegistryHandler.radiation, PE.getDuration()+1000));
//                } else {
//                    playerIn.addPotionEffect(new PotionEffect(RegistryHandler.radiation, 2000,1));
//                }

//                if(playerIn.hasCapability(RadiationLevelProvider.RADIATION_LEVEL_CAPABILITY, null)){
//                    IRadiationCapability radLevel = (IRadiationCapability) playerIn.getCapability(RadiationLevelProvider.RADIATION_LEVEL_CAPABILITY, null);
//                    playerIn.sendMessage(new TextComponentString("Rad level:" + radLevel.getRads()));
//                    radLevel.addRads(1000.0f);
//                    playerIn.sendMessage(new TextComponentString("New Rad level:" + radLevel.getRads()));
//                }
//                StageController.activateStaging(worldIn, null);
//               // DimensionManager.getWorld(0).isL
////                float radiation = RadiationController.getRadiationAbsorbedByPlayer(playerIn,RadiationController.emitRadiationFromSky(worldIn, playerIn.getPositionVector()), false);
////                playerIn.sendMessage(new TextComponentString("Radiation Left: " + radiation));
//                RadChunkProcessor r = new RadChunkProcessor(0,20);
                StageController.activateStaging(worldIn, null);


            }


            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
    }
}
