package net.tomatonet.nuclearwinter.staging;

public class StageSettings {
    private final boolean playerRadiationEnabled;
    private final Float radiationLevel;
    private final int nextStageTicks;


    public StageSettings(Builder builder) {
        this.playerRadiationEnabled = builder.isPlayerRadiationEnabled();
        this.radiationLevel = builder.getRadiationLevel();
        this.nextStageTicks = builder.getNextStageTicks();
    }

    public boolean isPlayerRadiationEnabled() {
        return this.playerRadiationEnabled;
    }

    public Float getRadiationLevel() {
        return radiationLevel;
    }

    public int getNextStageTicks() {
        return nextStageTicks;
    }

    public static class Builder {
        private boolean playerRadiationEnabled = false;
        private Float radiationLevel = 0.0f;
        private int nextStageTicks = 0;

        public StageSettings build() {
            return new StageSettings(this);
        }

        public Float getRadiationLevel() {
            return radiationLevel;
        }

        public Builder setRadiationLevel(Float radiationLevel) {
            if (radiationLevel < 0) {
                throw new IllegalArgumentException("Radiation level cannot be negative");
            }

            this.radiationLevel = radiationLevel;
            return this;
        }

        public Builder setPlayerRadiationEnabled(boolean playerRadiationEnabled) {
            this.playerRadiationEnabled = playerRadiationEnabled;
            return this;
        }

        public boolean isPlayerRadiationEnabled() {
            return playerRadiationEnabled;
        }

        public Builder setNextStageTicks(int i) {
            this.nextStageTicks = i;
            return this;
        }

        public Builder setNextStageDays(int days) {
            return setNextStageTicks(days * 24000);
        }

        public int getNextStageTicks() {
            return nextStageTicks;
        }
    }
}
