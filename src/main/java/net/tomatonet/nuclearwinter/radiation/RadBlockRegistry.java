package net.tomatonet.nuclearwinter.radiation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.tomatonet.nuclearwinter.NuclearWinter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RadBlockRegistry {
    private static Map<String, RadBlockSetting> loadedSettings = new HashMap<>();

    private static final RadBlockSetting DEFAULT_BLOCK_SETTING_FLUID = new RadBlockSetting("minecraft:water", 64.0f);
    private static final RadBlockSetting DEFAULT_BLOCK_SETTING_SOLID = new RadBlockSetting("minecraft:stone", 128.0f);

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
        ArrayList<RadFileSetting> fileSettings;
        try (var reader = new FileReader(file)) {
            var gson = new Gson();
            Type listType = new TypeToken<ArrayList<RadFileSetting>>() {}.getType();
            fileSettings = gson.fromJson(reader, listType);
        }
        
        var settingsTags = new ArrayList<RadFileSetting>();
        var settingsBlockId = new ArrayList<RadFileSetting>();
        
        for (var setting : fileSettings) {
            if (setting.isTaggedId()){
                settingsTags.add(setting);
                continue;
            }
            //TODO regex support here using a $ prefix
            settingsBlockId.add(setting);
        }

        //Process the tagged ids
        var taggedBlockSettings = getRadBlockSettingsFromTaggedIds(settingsTags);
        loadedSettings.putAll(taggedBlockSettings);

        for (var setting : settingsBlockId) {
            loadedSettings.putAll(getRadBlockSettingsFromFile(setting));
        }
        
        
    }

    private static Map<String,RadBlockSetting> getRadBlockSettingsFromTaggedIds(ArrayList<RadFileSetting> settingsTags) {
        var rtnMap = new HashMap<String, RadBlockSetting>();

        var tagMgr = ForgeRegistries.BLOCKS.tags();
        for(var setting : settingsTags){
            if(tagMgr.isKnownTagName(setting.toTagKey())) {
                //get the tag for the block
                var tag = tagMgr.getTag(setting.toTagKey());

                var blockMap = tag.stream().collect(Collectors.toMap(
                        block -> ForgeRegistries.BLOCKS.getKey(block).toString(),
                        block -> new RadBlockSetting(
                                ForgeRegistries.BLOCKS.getKey(block).toString(),
                                Float.parseFloat(setting.blockResistance()),
                                setting.blockDegradationLevel() != null ? Float.parseFloat(setting.blockDegradationLevel()) : null,
                                setting.targetBlockId() != null ? setting.targetBlockId() : null,
                                setting.stopOnDegradation()
                        )
                ));
                rtnMap.putAll(blockMap);
            }
            NuclearWinter.LOGGER.warn("Unknown tag: " + setting.resourceId());
        }

        return rtnMap;
    }


    private static Map<String, RadBlockSetting> getRadBlockSettingsFromFile(RadFileSetting settings) {
        if (settings.hasWildCard()) {
            return getRadBlockSettingsFromPattern(settings);
        }

        var radBlockSetting = settings.toRadBlockSetting();

        return Map.of(radBlockSetting.blockId(), radBlockSetting);
    }

    private static @NotNull HashMap<String, RadBlockSetting> getRadBlockSettingsFromPattern(RadFileSetting settings) {
        var rtnMap = new HashMap<String, RadBlockSetting>();
        var regex = settings.toRegexPattern();
        for (var block : getBlockIdsFromRegex(regex)) {
            var radBlockSetting = settings.toRadBlockSetting();
            rtnMap.put(radBlockSetting.blockId(), radBlockSetting);
        }
        if (rtnMap.isEmpty()) {
            NuclearWinter.LOGGER.warn("No blocks found for regex: " + settings.resourceId());
        }
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
        var blockId = ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).toString();
        if(loadedSettings.containsKey(blockId)) {
            return loadedSettings.get(blockId);
        }

        if (blockState.isAir()) {
            return null;
        } else if (blockState.isSolidRender(level, currentBlockPos)) {
            return DEFAULT_BLOCK_SETTING_SOLID;
        } else if (blockState.getFluidState().isSource()) {
            return DEFAULT_BLOCK_SETTING_FLUID;
        }

      return null;
    }
}
