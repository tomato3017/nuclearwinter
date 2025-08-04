package net.tomatonet.nuclearwinter.radiation;

public enum RadiationLevel {
    NONE(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    EXTREME(4);

    private final int level;

    RadiationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static RadiationLevel getLevel(int level) {
        for (RadiationLevel radiationLevel : RadiationLevel.values()) {
            if (radiationLevel.getLevel() == level) {
                return radiationLevel;
            }
        }

        return NONE;
    }
}
