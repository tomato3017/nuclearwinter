package net.tomatonet.nuclearwinter.radiation;

import com.google.gson.Gson;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class RadBlockSetting {

    private float blockResistance = 128.0f; //TODO Calculate based on explosion resistance
    public static RadBlockSetting getResistanceOfBlock(BlockState blockState, Level level, BlockPos currentBlockPos) {
        return new RadBlockSetting();
    }

    public static void loadSettings() {
       final Gson gson = new Gson();

    }

    public float getBlockResistance() {
        return this.blockResistance;
    }
}
