package net.tomatonet.nuclearwinter;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = NuclearWinter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue DAYS_CALM = BUILDER
            .comment("Number of days for the CALM stage (pre-apocalypse countdown)")
            .defineInRange("daysCalm", 7, 1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue DAYS_FALLOUT = BUILDER
            .comment("Number of days for the FALLOUT stage (early apocalypse)")
            .defineInRange("daysFallout", 7, 1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue DAYS_WASTELAND = BUILDER
            .comment("Number of days for the WASTELAND stage (peak apocalypse)")
            .defineInRange("daysWasteland", 10, 1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.BooleanValue DEBUG_LOGGING = BUILDER
            .comment("Enable debug logging")
            .define("debugLogging", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        NuclearWinter.LOGGER.debug("Loading config file {}", event.getConfig().getFileName());
    }
}
