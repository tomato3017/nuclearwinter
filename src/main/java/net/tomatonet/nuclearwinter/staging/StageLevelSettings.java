package net.tomatonet.nuclearwinter.staging;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class StageLevelSettings {

    public static final Capability<IStageLevelSettings> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IStageLevelSettings.class);
    }

    private StageLevelSettings() {
    }
}