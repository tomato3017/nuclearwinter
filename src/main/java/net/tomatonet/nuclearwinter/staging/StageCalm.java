package net.tomatonet.nuclearwinter.staging;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.tomatonet.nuclearwinter.Config;
import net.tomatonet.nuclearwinter.NuclearWinter;
import org.slf4j.Logger;

/**
 * Stage 0: CALM — The last days of normalcy.
 * A countdown timer builds tension with warning messages at milestones.
 * On the final day, the sky darkens with a forced thunderstorm.
 */
public class StageCalm extends StageBase {
    private static final Logger LOGGER = NuclearWinter.LOGGER;

    private static final long TICKS_3_DAYS = 3L * 24000L;
    private static final long TICKS_1_DAY = 24000L;
    private static final long TICKS_1_HOUR = 1000L;
    private static final long SKY_DARKEN_TICKS = 1000L;

    public StageCalm(ResourceLocation dimKey, long worldTickStart) {
        super(StageController.STAGES.CALM.toString(),
                dimKey,
                worldTickStart,
                new StageSettings.Builder()
                        .setNextStageDays(Config.DAYS_CALM.get())
                        .setWarningMessagesEnabled(true)
                        .build(),
                StageController.STAGES.CALM);
    }

    @Override
    public void doStageTick(Level level) {
        super.doStageTick(level);

        if (level instanceof ServerLevel serverLevel) {
            long ticksRemaining = getTicksLeftTillNextStage(level);

            if (getSettings().isWarningMessagesEnabled()) {
                checkWarningMilestones(serverLevel, ticksRemaining);
            }

            // Force thunderstorm for the last ~1000 ticks (sky darkening)
            if (ticksRemaining <= SKY_DARKEN_TICKS && ticksRemaining > 0) {
                serverLevel.setWeatherParameters(0, (int) ticksRemaining + 20, true, true);
            }
        }
    }

    private void checkWarningMilestones(ServerLevel level, long ticksRemaining) {
        // Stage ticks every 20 game ticks, so check a 20-tick window around each milestone
        if (isAtMilestone(ticksRemaining, TICKS_3_DAYS)) {
            broadcastWarning(level, "Nuclear fallout in 3 days. Seek shelter underground.");
        } else if (isAtMilestone(ticksRemaining, TICKS_1_DAY)) {
            broadcastWarning(level, "Nuclear fallout in 1 day! Get underground NOW!");
        } else if (isAtMilestone(ticksRemaining, TICKS_1_HOUR)) {
            broadcastWarning(level, "Nuclear fallout IMMINENT! Final warning!");
        }
    }

    private boolean isAtMilestone(long ticksRemaining, long milestone) {
        return ticksRemaining <= milestone && ticksRemaining > milestone - 20;
    }

    private void broadcastWarning(ServerLevel level, String message) {
        LOGGER.info("[NuclearWinter] Warning: {}", message);
        for (ServerPlayer player : level.players()) {
            player.sendSystemMessage(Component.literal("[Nuclear Winter] " + message));
        }
    }
}
