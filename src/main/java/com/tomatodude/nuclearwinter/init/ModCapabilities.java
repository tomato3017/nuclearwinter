package com.tomatodude.nuclearwinter.init;

import com.tomatodude.nuclearwinter.radiation.IRadiationCapability;
import com.tomatodude.nuclearwinter.radiation.RadiationLevel;
import com.tomatodude.nuclearwinter.radiation.RadiationLevelStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ModCapabilities {

    public static void register(){
        CapabilityManager.INSTANCE.register(IRadiationCapability.class, new RadiationLevelStorage(), RadiationLevel::new);
    }
}

