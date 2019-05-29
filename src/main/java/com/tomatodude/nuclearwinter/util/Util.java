package com.tomatodude.nuclearwinter.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class Util {
    public static void applyEffectIfReady(EntityPlayer player, Potion potion, int amplifier, int duration) {
        if(!player.isPotionActive(potion)) {
            player.addPotionEffect(new PotionEffect(potion, duration, amplifier));
        } else if (player.getActivePotionEffect(potion).getAmplifier() != amplifier){
            player.addPotionEffect(new PotionEffect(potion, duration, amplifier));
        } else if (player.getActivePotionEffect(potion).getDuration() < duration/2){
            player.addPotionEffect(new PotionEffect(potion, duration, amplifier));
        }
    }

    public static boolean isNetherOrEnd(World world) {
        return isDimension(world, -1) || isDimension(world, 1);
    }

    public static boolean isDimension(World world, int i) {
        return world.provider.getDimension() == i;
    }
}
