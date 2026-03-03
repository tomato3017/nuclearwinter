package net.tomatonet.nuclearwinter.staging;

public class StageSettings {
    private final boolean playerRadiationEnabled;
    private final Float radiationLevel;
    private final int nextStageTicks;
    private final boolean blockDegradationEnabled;
    private final DegradationScope degradationScope;
    private final boolean weatherLocked;
    private final LockedWeather lockedWeather;
    private final boolean radiationStormsEnabled;
    private final float stormRadiationMultiplier;
    private final boolean playerRadDecayEnabled;
    private final float playerRadDecayRate;
    private final boolean radiationLevelDecays;
    private final float radiationLevelMin;
    private final float radiationLevelDecayPerDay;
    private final boolean warningMessagesEnabled;

    public enum DegradationScope {
        NONE, ORGANIC_ONLY, ALL_CONFIGURED
    }

    public enum LockedWeather {
        NONE, CLEAR, OVERCAST, STORM
    }

    public StageSettings(Builder builder) {
        this.playerRadiationEnabled = builder.playerRadiationEnabled;
        this.radiationLevel = builder.radiationLevel;
        this.nextStageTicks = builder.nextStageTicks;
        this.blockDegradationEnabled = builder.blockDegradationEnabled;
        this.degradationScope = builder.degradationScope;
        this.weatherLocked = builder.weatherLocked;
        this.lockedWeather = builder.lockedWeather;
        this.radiationStormsEnabled = builder.radiationStormsEnabled;
        this.stormRadiationMultiplier = builder.stormRadiationMultiplier;
        this.playerRadDecayEnabled = builder.playerRadDecayEnabled;
        this.playerRadDecayRate = builder.playerRadDecayRate;
        this.radiationLevelDecays = builder.radiationLevelDecays;
        this.radiationLevelMin = builder.radiationLevelMin;
        this.radiationLevelDecayPerDay = builder.radiationLevelDecayPerDay;
        this.warningMessagesEnabled = builder.warningMessagesEnabled;
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

    public boolean isBlockDegradationEnabled() {
        return blockDegradationEnabled;
    }

    public DegradationScope getDegradationScope() {
        return degradationScope;
    }

    public boolean isWeatherLocked() {
        return weatherLocked;
    }

    public LockedWeather getLockedWeather() {
        return lockedWeather;
    }

    public boolean isRadiationStormsEnabled() {
        return radiationStormsEnabled;
    }

    public float getStormRadiationMultiplier() {
        return stormRadiationMultiplier;
    }

    public boolean isPlayerRadDecayEnabled() {
        return playerRadDecayEnabled;
    }

    public float getPlayerRadDecayRate() {
        return playerRadDecayRate;
    }

    public boolean isRadiationLevelDecays() {
        return radiationLevelDecays;
    }

    public float getRadiationLevelMin() {
        return radiationLevelMin;
    }

    public float getRadiationLevelDecayPerDay() {
        return radiationLevelDecayPerDay;
    }

    public boolean isWarningMessagesEnabled() {
        return warningMessagesEnabled;
    }

    public static class Builder {
        private boolean playerRadiationEnabled = false;
        private Float radiationLevel = 0.0f;
        private int nextStageTicks = 0;
        private boolean blockDegradationEnabled = false;
        private DegradationScope degradationScope = DegradationScope.NONE;
        private boolean weatherLocked = false;
        private LockedWeather lockedWeather = LockedWeather.NONE;
        private boolean radiationStormsEnabled = false;
        private float stormRadiationMultiplier = 2.0f;
        private boolean playerRadDecayEnabled = false;
        private float playerRadDecayRate = 50.0f;
        private boolean radiationLevelDecays = false;
        private float radiationLevelMin = 0.0f;
        private float radiationLevelDecayPerDay = 0.0f;
        private boolean warningMessagesEnabled = false;

        public StageSettings build() {
            return new StageSettings(this);
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

        public Builder setNextStageTicks(int i) {
            this.nextStageTicks = i;
            return this;
        }

        public Builder setNextStageDays(int days) {
            return setNextStageTicks(days * 24000);
        }

        public Builder setBlockDegradationEnabled(boolean blockDegradationEnabled) {
            this.blockDegradationEnabled = blockDegradationEnabled;
            return this;
        }

        public Builder setDegradationScope(DegradationScope degradationScope) {
            this.degradationScope = degradationScope;
            return this;
        }

        public Builder setWeatherLocked(boolean weatherLocked) {
            this.weatherLocked = weatherLocked;
            return this;
        }

        public Builder setLockedWeather(LockedWeather lockedWeather) {
            this.lockedWeather = lockedWeather;
            return this;
        }

        public Builder setRadiationStormsEnabled(boolean radiationStormsEnabled) {
            this.radiationStormsEnabled = radiationStormsEnabled;
            return this;
        }

        public Builder setStormRadiationMultiplier(float stormRadiationMultiplier) {
            this.stormRadiationMultiplier = stormRadiationMultiplier;
            return this;
        }

        public Builder setPlayerRadDecayEnabled(boolean playerRadDecayEnabled) {
            this.playerRadDecayEnabled = playerRadDecayEnabled;
            return this;
        }

        public Builder setPlayerRadDecayRate(float playerRadDecayRate) {
            this.playerRadDecayRate = playerRadDecayRate;
            return this;
        }

        public Builder setRadiationLevelDecays(boolean radiationLevelDecays) {
            this.radiationLevelDecays = radiationLevelDecays;
            return this;
        }

        public Builder setRadiationLevelMin(float radiationLevelMin) {
            this.radiationLevelMin = radiationLevelMin;
            return this;
        }

        public Builder setRadiationLevelDecayPerDay(float radiationLevelDecayPerDay) {
            this.radiationLevelDecayPerDay = radiationLevelDecayPerDay;
            return this;
        }

        public Builder setWarningMessagesEnabled(boolean warningMessagesEnabled) {
            this.warningMessagesEnabled = warningMessagesEnabled;
            return this;
        }
    }
}
