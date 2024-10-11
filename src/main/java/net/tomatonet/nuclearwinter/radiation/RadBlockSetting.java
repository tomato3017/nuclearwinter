package net.tomatonet.nuclearwinter.radiation;

//public class RadBlockSetting {
//
//    private float blockResistance = 128.0f; //TODO Calculate based on explosion resistance
//    public static RadBlockSetting getResistanceOfBlock(BlockState blockState, Level level, BlockPos currentBlockPos) {
//        return new RadBlockSetting();
//    }
//
//    public static void loadSettings() {
//       final Gson gson = new Gson();
//
//    }
//
//    public float getBlockResistance() {
//        return this.blockResistance;
//    }
//}

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public record RadBlockSetting(
        String blockId,
        float blockResistance,
        Float blockDegradationLevel,
        String targetBlockId
) {

    public float getBlockResistance() {
        return 128.0f;
    }

    public Block getBlock() {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId));
    }

    public String toString() {
        return String.format("ID: %s, Resistance: %.2f, Degradation Level: %s, Target Block ID: %s",
                blockId, blockResistance, blockDegradationLevel, targetBlockId);
    }
}