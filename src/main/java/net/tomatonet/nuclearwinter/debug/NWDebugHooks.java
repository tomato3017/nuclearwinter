package net.tomatonet.nuclearwinter.debug;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.tomatonet.nuclearwinter.NuclearWinter;
import net.tomatonet.nuclearwinter.radiation.RadBlockSetting;
import net.tomatonet.nuclearwinter.radiation.RadiationSettings;
import net.tomatonet.nuclearwinter.radiation.RadiationSource;
import net.tomatonet.nuclearwinter.staging.StageController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        dumpAllBlockIds();
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

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START && NuclearWinter.stageController.isLoadedStage(event.player.level())) {
            Player player = event.player;
            if (player.level().getGameTime() % 20 == 0) {
                RadiationSettings settings = new RadiationSettings().setBlockLightDegradation(false);
                RadiationSource skyRads = new RadiationSource(settings);
                float radsGiven = skyRads.emitRadiation(player.level(),
                        RadiationSource.getHeightMapPos(player.level(), player.blockPosition()).add(0,1,0),
                        new Vec3(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                if (radsGiven > 0) {
                    NuclearWinter.LOGGER.debug("Player " + player.getName().getString() + " received " + radsGiven + " rads");
                }
            }
        }
    }
}
