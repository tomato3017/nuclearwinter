package net.tomatonet.nuclearwinter.radiation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

//TODO convert this to a class so I can enforce runtime null checks
public record RadFileSetting(
        @NotNull String resourceId,
        @NotNull String blockResistance,
        String blockDegradationLevel,
        String targetBlockId,
        boolean stopOnDegradation
) {
    public RadFileSetting(@NotNull String resourceId, @NotNull String blockResistance, String blockDegradationLevel, String targetBlockId) {
        this(resourceId, blockResistance, blockDegradationLevel, targetBlockId, false);
    }
    public boolean hasWildCard() {
        return resourceId.contains("*");
    }

    public boolean isTaggedId() {
        return resourceId.startsWith("#");
    }

    public String untaggedId() {
        return resourceId.replace("#","");
    }

    public TagKey<Block> toTagKey() {
        if(!isTaggedId()) {
            return null;
        }

        return TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), new ResourceLocation(untaggedId()));
    }

    public Pattern toRegexPattern() {
        var tokenizedBlockId = resourceId.replace(".*", "<DOT_STAR>").
                replace("*", ".*").
                replace("<DOT_STAR>", ".*");
        return Pattern.compile(tokenizedBlockId);
    }

    public RadBlockSetting toRadBlockSetting() {
        return new RadBlockSetting(
                resourceId,
                Float.parseFloat(blockResistance),
                blockDegradationLevel != null ? Float.parseFloat(blockDegradationLevel) : null,
                targetBlockId,
                stopOnDegradation
        );
    }

}
