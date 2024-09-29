package net.tomatonet.nuclearwinter.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.tomatonet.nuclearwinter.NuclearWinter;
import net.tomatonet.nuclearwinter.staging.IStageLevelSettings;
import net.tomatonet.nuclearwinter.staging.StageBase;
import net.tomatonet.nuclearwinter.staging.StageController;
import org.jetbrains.annotations.NotNull;

public class NuclearWinterCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("nuclearwinter").
                then(Commands.literal("start").executes(NuclearWinterCommand::runCmdStart)).
                then(Commands.literal("stop").executes(NuclearWinterCommand::runCmdStop)).
                then(Commands.literal("status").executes(NuclearWinterCommand::runCmdStatus)));
    }

    private static int runCmdStatus(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        if (commandSourceStackCommandContext.getSource().getEntity() instanceof Player player) {
            if (NuclearWinter.stageController.isLoadedStage(player.level())) {
                IStageLevelSettings stageSettings = NuclearWinter.stageController.getStageLevelSettings(player.level());
                StageBase loadedStage = NuclearWinter.stageController.getActiveStage(player.level());

                player.sendSystemMessage(Component.literal(getStageStatus(stageSettings, loadedStage)));
            } else {
                player.sendSystemMessage(Component.literal("Staging is not active!"));
            }
            return Command.SINGLE_SUCCESS;
        }
        commandSourceStackCommandContext.getSource().sendFailure(Component.literal("Command source is not a player! You must be a player to run this command!"));
        return 0;
    }

    private static @NotNull String getStageStatus(IStageLevelSettings stageSettings, StageBase loadedStage) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(String.format("Staging is active! Current stage: %s\n", stageSettings.getCurrentStage().name()));
        sBuilder.append(String.format("Stage type: %s\n", loadedStage.getStageType().name()));
        sBuilder.append(String.format("Stage name: %s\n", loadedStage.getName()));
        sBuilder.append(String.format("Stage dim key: %s\n", loadedStage.getDimKey()));
        sBuilder.append(String.format("Stage world tick start: %s\n", loadedStage.getWorldTickStart()));
        return sBuilder.toString();
    }

    private static int runCmdStop(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        if (commandSourceStackCommandContext.getSource().getEntity() instanceof Player player) {
            if (NuclearWinter.stageController.hasStageLevelSettings(player.level())) {
                if (!NuclearWinter.stageController.isLoadedStage(player.level())) {
                    commandSourceStackCommandContext.getSource().sendFailure(Component.literal("Stage is not loaded!"));
                    return 0;
                }
            }

            NuclearWinter.stageController.deactivateStaging(player.level());
            player.sendSystemMessage(Component.literal("Staging deactivated!"));

            return Command.SINGLE_SUCCESS;
        }
        commandSourceStackCommandContext.getSource().sendFailure(Component.literal("Command source is not a player! You must be a player to run this command!"));
        return 0;
    }

    private static int runCmdStart(CommandContext<CommandSourceStack> command) {
        if (command.getSource().getEntity() instanceof Player player) {
            if (NuclearWinter.stageController.isLoadedStage(player.level())) {
                command.getSource().sendFailure(Component.literal("Stage is already loaded!"));
                return 0;
            }

            if (NuclearWinter.stageController.hasStageLevelSettings(player.level())) {
                IStageLevelSettings stageSettings = NuclearWinter.stageController.getStageLevelSettings(player.level());
                NuclearWinter.stageController.activateStaging(player.level(), stageSettings.getCurrentStage());
            } else {
                //TODO Should we set this to ApocLow?
                NuclearWinter.stageController.activateStaging(player.level(), StageController.STAGES.PREAPOC);
            }
            player.sendSystemMessage(Component.literal("Staging activated!"));
            return Command.SINGLE_SUCCESS;
        }
        command.getSource().sendFailure(Component.literal("Command source is not a player! You must be a player to run this command!"));
        return 0;
    }
}
