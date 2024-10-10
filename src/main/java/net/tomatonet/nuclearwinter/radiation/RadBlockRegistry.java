package net.tomatonet.nuclearwinter.radiation;

import com.google.gson.Gson;
import net.minecraftforge.fml.loading.FMLPaths;
import net.tomatonet.nuclearwinter.NuclearWinter;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class RadBlockRegistry {
    private static Map<String, RadBlockSetting> loadedSettings = new HashMap<>();

    public static void loadSettingsFromFile() {
        // Load settings from file
        var dir = FMLPaths.CONFIGDIR.get().resolve("nuclearwinter/radsettings").toFile();
        if (!dir.exists()){
            dir.mkdirs();
        }
        // Load settings from files in the directory
        for (var file : dir.listFiles()) {
            if (file.getName().endsWith(".json")) {
                NuclearWinter.LOGGER.debug("Loading settings from file: " + file.getName());
                loadFile(file);
            }
        }

    }

    private static void loadFile(File file) {
        // Load settings from file
        try (var reader = new FileReader(file)) {


        } catch (Exception e) {
            NuclearWinter.LOGGER.error("An error occurred while loading radiation settings from file: " + file.getName(), e);
        }
    }

}
