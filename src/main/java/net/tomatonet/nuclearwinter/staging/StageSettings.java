package net.tomatonet.nuclearwinter.staging;

public record StageSettings(String name, ) {
    public static class Builder {
        private long ticksTillNextStage;
    }
}
