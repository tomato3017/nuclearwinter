package net.tomatonet.nuclearwinter.staging;

public class StageSettings {
    private final boolean playerRadiationEnabled;
    private final Float radiationLevel;


    public StageSettings(Builder builder) {
        this.playerRadiationEnabled = builder.isPlayerRadiationEnabled();
        this.radiationLevel = builder.getRadiationLevel();
    }

    public boolean isPlayerRadiationEnabled() {
        return this.playerRadiationEnabled;
    }

    public Float getRadiationLevel() {
        return radiationLevel;
    }

    public static class Builder {
        private boolean playerRadiationEnabled = false;
        private Float radiationLevel = 1000.0f;

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
    }
}
