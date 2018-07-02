package com.tomatodude.nuclearwinter.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

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
}
