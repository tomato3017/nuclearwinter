package net.tomatonet.nuclearwinter.debug;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.tomatonet.nuclearwinter.NuclearWinter;
import net.tomatonet.nuclearwinter.radiation.RadiationConfig;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Mod.EventBusSubscriber(modid = NuclearWinter.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NWDebugHooks {

    public static void dumpBlockDestroySpeeds(String filePath) {
        List<Block> blocks = ForgeRegistries.BLOCKS.getValues().stream().toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Block block : blocks) {
                BlockState defaultState = block.defaultBlockState();
                float destroySpeed = defaultState.getDestroySpeed(null, null);
                writer.write(ForgeRegistries.BLOCKS.getKey(block) + ": " + destroySpeed);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dumpAllBlockIds() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("block_ids.txt"))) {
            for (Block block : ForgeRegistries.BLOCKS) {
                ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
                writer.write(blockId.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        //TODO remove
        dumpBlockDestroySpeeds("block_destroy_speeds.txt");
        dumpBlockTags("block_tags.txt");
        dumpBiomes("biomes.txt", event.getServer());
        dumpAllBlockIds();
        NuclearWinter.LOGGER.debug("Expected Radiation Levels: ");
        NuclearWinter.LOGGER.debug("\t" + RadiationConfig.RADIATION4_RAD_LEVEL + " - EXTREME");
        NuclearWinter.LOGGER.debug("\t" + RadiationConfig.RADIATION3_RAD_LEVEL + " - HIGH");
        NuclearWinter.LOGGER.debug("\t" + RadiationConfig.RADIATION2_RAD_LEVEL + " - MEDIUM");
        NuclearWinter.LOGGER.debug("\t" + RadiationConfig.RADIATION1_RAD_LEVEL + " - LOW");
    }

    public static void dumpBlockTags(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Block block : ForgeRegistries.BLOCKS) {
                writer.write(ForgeRegistries.BLOCKS.getKey(block) + ":\t");
                for (var tag : block.defaultBlockState().getTags().toList()) {
                    writer.write(tag.location() + ", ");
                }

                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dumpBiomes(String filePath, MinecraftServer server) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            var registry = server.registryAccess();
            var biomeRegistry = registry.registryOrThrow(Registries.BIOME);
            for (Biome biome : biomeRegistry) {
                writer.write(biomeRegistry.getKey(biome).toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        registerTestCmd(event.getDispatcher());
    }


    public static void registerTestCmd(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("nwtest").executes(NWDebugHooks::runTest));
    }

    private static int runTest(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        if (commandSourceStackCommandContext.getSource().getEntity() instanceof Player player) {
            var level = player.level();
            var chunk = level.getChunkAt(player.blockPosition());

            var seaLevel = new BlockPos(player.blockPosition().getX(), 64, player.blockPosition().getZ());
            int surfaceSectionIndex = level.getSectionIndex(seaLevel.getY());

            for (int currentSec = level.getMaxSection(); currentSec > surfaceSectionIndex; currentSec--) {
                PalettedContainer<Holder<Biome>> biomes = chunk.getSection(currentSec).getBiomes().recreate();

                ResourceKey<Biome> newBiomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES,
                        new ResourceLocation("minecraft:snowy_taiga"));
                Holder<Biome> newBiomeHolder = level.registryAccess().registryOrThrow(ForgeRegistries.Keys.BIOMES).getHolderOrThrow(newBiomeKey);

                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                        for (int z = 0; z < 4; z++) {
                            biomes.set(x, y, z, newBiomeHolder);
                        }
                    }
                }
            }
            chunk.setUnsaved(true);
        }


        return Command.SINGLE_SUCCESS;
    }

//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (event.side.isServer() && event.phase == TickEvent.Phase.START && NuclearWinter.stageController.isLoadedStage(event.player.level())) {
//            Player player = event.player;
//            if (player.level().getGameTime() % 20 == 0) {
//                RadiationSettings settings = new RadiationSettings().setBlockLightDegradation(false);
//                RadiationSource skyRads = new RadiationSource(settings);
//                float radsGiven = skyRads.emitRadiation(player.level(),
//                        RadiationSource.getHeightMapPos(player.level(), player.blockPosition()).add(0,1,0),
//                        new Vec3(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
//                if (radsGiven > 0) {
//                    NuclearWinter.LOGGER.debug("Player " + player.getName().getString() + " received " + radsGiven + " rads");
//                }
//            }
//        }
//    }
}
