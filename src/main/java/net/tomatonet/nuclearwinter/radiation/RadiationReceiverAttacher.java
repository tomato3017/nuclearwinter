package net.tomatonet.nuclearwinter.radiation;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.tomatonet.nuclearwinter.NuclearWinter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RadiationReceiverAttacher {
    private static class RadiationReceiverProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        //TODO look at this
        public static final ResourceLocation IDENTIFIER = new ResourceLocation(NuclearWinter.MODID, "radiation_received");

        private final IRadiationReceiver backend = new RadiationReceiverImplementer();
        private final LazyOptional<IRadiationReceiver> optionalData = LazyOptional.of(() -> backend);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return RadiationReceiver.INSTANCE.orEmpty(cap, this.optionalData);
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

    public static void attach(final AttachCapabilitiesEvent<Player> event) {
        final RadiationReceiverProvider provider = new RadiationReceiverAttacher.RadiationReceiverProvider();

        event.addCapability(RadiationReceiverProvider.IDENTIFIER, provider);
    }
}
