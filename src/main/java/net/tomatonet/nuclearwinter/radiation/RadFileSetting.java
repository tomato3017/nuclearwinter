package net.tomatonet.nuclearwinter.radiation;

import java.util.regex.Pattern;

public record RadFileSetting(
        String blockId,
        String blockResistance,
        String blockDegradationLevel,
        String targetBlockId
) {
    public boolean hasWildCard() {
        return blockId.contains("*");
    }

    public Pattern toRegexPattern() {
        var tokenizedBlockId = blockId.replace(".*", "<DOT_STAR>").
                replace("*", ".*").
                replace("<DOT_STAR>", ".*");
        return Pattern.compile(tokenizedBlockId);
    }

}
