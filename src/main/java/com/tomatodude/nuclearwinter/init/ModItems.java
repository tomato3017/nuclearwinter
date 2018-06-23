package com.tomatodude.nuclearwinter.init;

import com.tomatodude.nuclearwinter.NuclearWinter;
import com.tomatodude.nuclearwinter.items.ItemBase;
import com.tomatodude.nuclearwinter.items.armor.ArmorBase;
import com.tomatodude.nuclearwinter.util.Reference;

import java.util.ArrayList;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
	public static final List<Item> ITEMS = new ArrayList<>();

	//Materials
	public static final ArmorMaterial MATERIAL_HAZMAT_SWATCH = EnumHelper.addArmorMaterial("Hazmat_Swatch", Reference.MOD_ID + "Hazmat_Swatch", 3,
				new int[] {2,2,2,2}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	
	
	//Items
	public static final Item HAZMAT_SWATCH = new ItemBase("Hazmat_Swatch");
	public static final Item RADIATION_BADGE = new ItemBase("Radiation_Badge");
	public static final Item GEIGAR_COUNTER = new ItemBase("Geigar_Counter");

	//Armor
	public static final Item HAZMAT_SUIT = new ArmorBase("Hazmat_Suit",MATERIAL_HAZMAT_SWATCH,1,EntityEquipmentSlot.CHEST);
	public static final Item HAZMAT_HELMET = new ArmorBase("Hazmat_Helmet",MATERIAL_HAZMAT_SWATCH,1,EntityEquipmentSlot.HEAD);
	public static final Item HAZMAT_BOOTS = new ArmorBase("Hazmat_Boots",MATERIAL_HAZMAT_SWATCH,1,EntityEquipmentSlot.FEET);
	public static final Item HAZMAT_LEGGINS = new ArmorBase("Hazmat_Leggings",MATERIAL_HAZMAT_SWATCH,2,EntityEquipmentSlot.LEGS);

	//Debug
	public static final Item RADIATION_DEBUGGER = new ItemBase("Radiation_Debugger");

    public static void registerItems(RegistryEvent.Register<Item> event){
        for (Item i : ITEMS) {
            NuclearWinter.logger.info("Registering item " + i.getUnlocalizedName());
            event.getRegistry().register(i);
        }
    }

}
