package com.tomatodude.nuclearwinter.effect;

import com.tomatodude.nuclearwinter.radiation.RadiationController;
import com.tomatodude.nuclearwinter.util.RadiationConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;

import static com.tomatodude.nuclearwinter.util.Util.applyEffectIfReady;

public class PotionRadiation extends Potion {

    public PotionRadiation(String name, String id, double effectiveness) {
        super(true, 0);
        setPotionName(name);
        setRegistryName(id);
        setEffectiveness(effectiveness);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
        if(entityLivingBaseIn instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) entityLivingBaseIn;

            if(amplifier>=2){
                applyEffectIfReady(player,MobEffects.BLINDNESS, 1,400);
                applyEffectIfReady(player, MobEffects.HUNGER, 1, 400);
                applyBasicRadiationEffects(player);
                damagePlayer(player, RadiationConfig.RADIATION3_DAMAGE, 40);
            } else if(amplifier>= 1){
                applyEffectIfReady(player, MobEffects.HUNGER, 0, 400);
                applyBasicRadiationEffects(player);
                damagePlayer(player, RadiationConfig.RADIATION2_DAMAGE, 80);
            } else {
                applyBasicRadiationEffects(player);
            }
        }
    }

    public void damagePlayer(EntityPlayer player,int damage, long ticksBetween) {
        long tick = player.getEntityWorld().getWorldTime();
        if(tick%ticksBetween == 0){ //Apply every second
            player.attackEntityFrom(RadiationController.RADIATION_DMG,damage);
        }
    }

    private void applyBasicRadiationEffects(EntityPlayer player) {
        applyEffectIfReady(player, MobEffects.MINING_FATIGUE, 1, 400);
        applyEffectIfReady(player, MobEffects.SLOWNESS, 1, 400);
    }
}
