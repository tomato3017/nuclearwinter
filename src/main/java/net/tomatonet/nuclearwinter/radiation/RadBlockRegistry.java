package net.tomatonet.nuclearwinter.radiation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.tomatonet.nuclearwinter.NuclearWinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RadBlockRegistry {
    private static Map<String, RadBlockSetting> loadedSettings = new HashMap<>();

    public static void loadSettingsFromFile() {
        // Load settings from file
        var dir = FMLPaths.CONFIGDIR.get().resolve("nuclearwinter/radsettings").toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Load settings from files in the directory
        for (var file : dir.listFiles()) {
            if (file.getName().endsWith(".json")) {
                NuclearWinter.LOGGER.info("Loading settings from file: " + file.getName());
                try {
                    loadFile(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for(var entry : loadedSettings.entrySet()) {
            NuclearWinter.LOGGER.debug("Loaded block resistance definition: " + entry.getKey() + " -> " + entry.getValue().toString());
        }

        NuclearWinter.LOGGER.info("Loaded " + loadedSettings.size() + " block resistance definitions");

    }

    private static void loadFile(File file) throws IOException {
        // Load settings from file
        try (var reader = new FileReader(file)) {
            var gson = new Gson();
            Type listType = new TypeToken<ArrayList<RadFileSetting>>() {}.getType();
            ArrayList<RadFileSetting> fileSettings = gson.fromJson(reader, listType);
            for (var setting : fileSettings) {
                var radBlockSettings = getRadBlockSettingsFromFile(setting);
                putRadBlockSetting(radBlockSettings);
            }
        }
    }

    private static void putRadBlockSetting(Map<String, RadBlockSetting> radBlockSettings) {
        for (var entry : radBlockSettings.entrySet()) {
            if (loadedSettings.containsKey(entry.getKey())) {
                NuclearWinter.LOGGER.warn("Block resistance definition already exists: " + entry.getKey());
                throw new RuntimeException("Block resistance definition already exists: " + entry.getKey());
            }
        }
        loadedSettings.putAll(radBlockSettings);
    }

    private static Map<String, RadBlockSetting> getRadBlockSettingsFromFile(RadFileSetting settings) {
        var rtnMap = new HashMap<String, RadBlockSetting>();
        if (settings.hasWildCard()) {
            var regex = settings.toRegexPattern();
            for (var block : getBlockIdsFromRegex(regex)) {
                var radBlockSetting = new RadBlockSetting(
                        block.toString(),
                        Float.parseFloat(settings.blockResistance()),
                        settings.blockDegradationLevel() != null ? Float.parseFloat(settings.blockDegradationLevel()) : null,
                        settings.targetBlockId() != null ? settings.targetBlockId() : null
                );
                rtnMap.put(radBlockSetting.blockId(), radBlockSetting);
            }
            if (rtnMap.isEmpty()) {
                NuclearWinter.LOGGER.warn("No blocks found for regex: " + settings.blockId());
            }
            return rtnMap;
        }

        var radBlockSetting = new RadBlockSetting(
                settings.blockId(),
                Float.parseFloat(settings.blockResistance()),
                settings.blockDegradationLevel() != null ? Float.parseFloat(settings.blockDegradationLevel()) : null,
                settings.targetBlockId() != null ? settings.targetBlockId() : null
        );

        rtnMap.put(radBlockSetting.blockId(), radBlockSetting);
        return rtnMap;
    }

    private static List<ResourceLocation> getBlockIdsFromRegex(Pattern regex) {
        var rtnList = new ArrayList<ResourceLocation>();
        for (var block : ForgeRegistries.BLOCKS.getValues()) {
            var blockId = ForgeRegistries.BLOCKS.getKey(block);
            if (regex.matcher(blockId.toString()).matches()) {
                rtnList.add(blockId);
            }
        }

        return rtnList;
    }

    public static RadBlockSetting getResistanceOfBlock(BlockState blockState, Level level, BlockPos currentBlockPos) {
        return new RadBlockSetting("minecraft:stone", 128.0f, null, null);
    }
}
