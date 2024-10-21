package net.tomatonet.nuclearwinter.radiation;

import java.util.HashMap;

//TODO: This is temporary, we need to make this all in a config
public class RadiationConfig {
    public static final int RAD_MAX_LIGHT_LEVEL = 7;
    public static final int RAD_DEATH_LEVEL = 50000;

    //Default Block resistances.
    public static final float BLOCK_RESIST_HARD_LOW = 16.0f;
    public static final float BLOCK_RESIST_HARD_MED = 128.0f;
    public static final float BLOCK_RESIST_HARD_HIGH = 230.0f;

    //Radiation Damage
    public static final int RADIATION2_DAMAGE = 1;
    public static final int RADIATION3_DAMAGE = 4;

    //Rad level that the user gets radiation at.
    public static final float RADIATION4_RAD_LEVEL = 50000;
    public static final float RADIATION3_RAD_LEVEL = 10000;
    public static final float RADIATION2_RAD_LEVEL = 1000;
    public static final float RADIATION1_RAD_LEVEL = 100;

    //Max radiation allowed to be emitted //TODO: Make this stage dependent
    public static final int MAX_RADIATION_LEVEL = 50000;

    //Where radiation emitted from the sky starts
    public static final float PLAYER_NATURAL_RESISTANCE = 100;

    //Armor Resistances
    public static final float HAZMAT_BOOTS_RESISTANCE = 128;
    public static final float HAZMAT_HELMET_RESISTANCE = 128;
    public static final float HAZMAT_LEGS_RESISTANCE = 128;
    public static final float HAZMAT_SUIT_RESISTANCE = 128;
    public static final double MAX_RADIATION_LEVEL_PREAPOC = 512;
    public static final int CHUNKPROC_CHUNK_PER_TICK = 40;
    public static final int CHUNKPROC_BLOCKSTATIC_PER_TICK = 1;
    public static final int CHUNKPROC_BLOCKRANDOM_PER_TICK = 20;

    //Stores custom radiation settings
    private static HashMap<String,RadBlockSetting> radiationMap;


    public static final int RAD_BLOCK_LIGHT_DEGRADE_FACTOR = 6;
    public static final boolean RAD_BLOCK_LIGHT_DEGRADE = true;

//
//    public static void initRadMap(){
//        radiationMap = new HashMap<>();
//
//        insertMapping(new RadBlockSetting("minecraft:dirt", 16));
//        insertMapping(new RadBlockSetting("minecraft:grass", 16)
//                .setBlockDegradationLevel(511)
//                .setDegradedBlockName("minecraft:dirt")
//                .setDegradedBlockMetaID(1));
//        insertMapping(new RadBlockSetting("minecraft:stone", 128));
//
//    }

//    private static void insertMapping(RadBlockSetting setting){
//        if(setting.isDefaultBlockState()) {
//            radiationMap.put(setting.getBlockName(), setting);
//        } else {
//            radiationMap.put(setting.getFullBlockResourceName(), setting);
//        }
//    }

//    public static RadBlockSetting getMapping(String blockName, int metaID){
//        String fullResourceName = blockName + ":" + metaID;
//        if(radiationMap.containsKey(fullResourceName)){
//            return radiationMap.get(fullResourceName);
//        } else if (radiationMap.containsKey(blockName)){
//            return radiationMap.get(blockName);
//        }
//
//        return null;
//    }
}
