package com.tomatodude.nuclearwinter.items.armor;

import com.tomatodude.nuclearwinter.NuclearWinter;
import com.tomatodude.nuclearwinter.init.ModItems;
import com.tomatodude.nuclearwinter.radiation.IRadResistent;
import com.tomatodude.nuclearwinter.util.IHasModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ArmorBase extends ItemArmor implements IHasModel, IRadResistent {

	public ArmorBase(String name,ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.COMBAT);
		
		ModItems.ITEMS.add(this);
	}

	@Override
	public void registerModels() {
		NuclearWinter.proxy.registerItemRenderer(this, 0, "inventory");
	}

    @Override
    public abstract float getRadResistance();

	@Override
	public boolean canRadDamageArmor() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		float radResistance = this.getRadResistance();
		tooltip.add("Rad Resistance: " + String.valueOf(radResistance));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
