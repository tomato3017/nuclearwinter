package net.tomatonet.nuclearwinter.radiation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public record RadBlockSetting(
        String blockId,
        float blockResistance,
        Float blockDegradationLevel,
        String targetBlockId,
        boolean stopOnDegradation
) {
    public RadBlockSetting(String blockId, float blockResistance) {
        this(blockId, blockResistance, null, null, false);
    }

    public RadBlockSetting(String blockId, float blockResistance, Float blockDegradationLevel, String targetBlockId) {
       this(blockId, blockResistance, blockDegradationLevel, targetBlockId, false);
    }





    public Block getBlock() {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId));
    }

    public String toString() {
        return String.format("ID: %s, Resistance: %.2f, Degradation Level: %s, Target Block ID: %s, Stop on Degradation: %b",
                blockId, blockResistance, blockDegradationLevel, targetBlockId, stopOnDegradation);
    }

    public boolean isBlockDegrade() {
        return blockDegradationLevel != null;
    }

    public BlockState targetBlockState() {
        if (targetBlockId == null) {
            return null;
        }
        if (ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(targetBlockId))) {
            return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(targetBlockId)).defaultBlockState();
        }

        return null;
    }
}