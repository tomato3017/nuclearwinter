package net.tomatonet.nuclearwinter.staging;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.tomatonet.nuclearwinter.NuclearWinter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class StageLevelSettingsAttacher {

    private static class StageLevelSettingsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        //TODO look at this
        public static final ResourceLocation IDENTIFIER = new ResourceLocation(NuclearWinter.MODID, "stage_level_settings");

        private final IStageLevelSettings backend = new StageLevelSettingsImplementer();
        private final LazyOptional<IStageLevelSettings> optionalData = LazyOptional.of(() -> backend);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return StageLevelSettings.INSTANCE.orEmpty(cap, this.optionalData);
        }

        void invalidate() {
            this.optionalData.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    public static void attach(final AttachCapabilitiesEvent<Level> event) {
        final StageLevelSettingsProvider provider = new StageLevelSettingsProvider();

        event.addCapability(StageLevelSettingsProvider.IDENTIFIER, provider);
    }

    private StageLevelSettingsAttacher() {
    }
}
