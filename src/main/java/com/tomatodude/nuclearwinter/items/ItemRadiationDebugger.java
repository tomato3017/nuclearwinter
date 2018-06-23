package com.tomatodude.nuclearwinter.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemRadiationDebugger extends ItemBase {
    private static final String ITEM_NAME = "Radiation_Debugger";

    public ItemRadiationDebugger() {
        super(ITEM_NAME);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStackIn = playerIn.getHeldItem(handIn);
        playerIn.sendMessage(new TextComponentString("Test Message!!"));

        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
    }
}
