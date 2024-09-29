package net.tomatonet.nuclearwinter;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import io.netty.util.Attribute;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = NuclearWinter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

//    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
//            .comment("Whether to log the dirt block on common setup")
//            .define("logDirtBlock", true);

//    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
//            .comment("A magic number")
//            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);
//
//    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
//            .comment("What you want the introduction message to be for the magic number")
//            .define("magicNumberIntroduction", "The magic number is... ");
//
//    // a list of strings that are treated as resource locations for items
//    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
//            .comment("A list of items to log on common setup.")
//            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);
    public static final ForgeConfigSpec.IntValue DAYS_BEFORE_APOCALYPSE = BUILDER
            .comment("Number of days before the apocalypse")
            .defineInRange("daysBeforeApocalypse", 7, 1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue DAYS_APOCALYPSE_LOW = BUILDER
            .comment("Number of days the apocalypse will last on low intensity")
            .defineInRange("daysApocalypseLow", 7, 1, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        NuclearWinter.LOGGER.debug("Loading config file {}", event.getConfig().getFileName());
        for (Map.Entry<String, ForgeConfigSpec.ConfigValue<?>> entry : event.getConfig().getConfigData().) {
            NuclearWinter.LOGGER.debug("{}: {}", entry.getKey(), entry.getValue());
        }
    }
}
