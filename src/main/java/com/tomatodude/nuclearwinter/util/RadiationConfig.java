package com.tomatodude.nuclearwinter.util;

import com.tomatodude.nuclearwinter.radiation.RadBlockSetting;

import java.util.HashMap;

//TODO: Load from config file
public class RadiationConfig {
    public static final int RAD_MAX_LIGHT_LEVEL = 12;
    public static final float BLOCK_RESIST_HARD_LOW = 16.0f;
    public static final float BLOCK_RESIST_HARD_MED = 128.0f;
    public static final float BLOCK_RESIST_HARD_HIGH = 230.0f;
    private static HashMap<String, RadBlockSetting> radiationMap;

    public static final int RAD_SKY_DEGRADE_FACTOR = 96;


    public static void initRadMap(){
        radiationMap = new HashMap<>();

        insertMapping(new RadBlockSetting("minecraft:dirt", 16));
        insertMapping(new RadBlockSetting("minecraft:grass", 16,
                "minecraft:dirt", 512));
        insertMapping(new RadBlockSetting("minecraft:stone", 128));

    }

    public static void insertMapping(RadBlockSetting setting){
        radiationMap.put(setting.getBlockName(), setting);
    }

    public static RadBlockSetting getMapping(String blockName){
        return radiationMap.get(blockName);
    }
}
