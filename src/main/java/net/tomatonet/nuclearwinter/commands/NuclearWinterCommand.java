package net.tomatonet.nuclearwinter.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.tomatonet.nuclearwinter.NuclearWinter;
import net.tomatonet.nuclearwinter.capabilities.CapabiltiesAttacher;
import net.tomatonet.nuclearwinter.staging.IStageLevelSettings;
import net.tomatonet.nuclearwinter.staging.StageBase;
import net.tomatonet.nuclearwinter.staging.StageController;
import org.jetbrains.annotations.NotNull;

public class NuclearWinterCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("nuclearwinter").
                then(Commands.literal("start").executes(NuclearWinterCommand::runCmdStart)).
                then(Commands.literal("stop").executes(NuclearWinterCommand::runCmdStop)).
                then(Commands.literal("status").executes(NuclearWinterCommand::runCmdStatus)).
                then(Commands.literal("setstage").
                        then(Commands.argument("stageName", StringArgumentType.string())).
                        executes(NuclearWinterCommand::runSetStage)));
    }

    private static int runSetStage(CommandContext<CommandSourceStack> commandIn) {
        if (commandIn.getSource().getEntity() instanceof Player player) {
            if (!NuclearWinter.stageController.isLoadedStage(player.level())) {
                commandIn.getSource().sendFailure(Component.literal("You must first start!"));
                return 0;
            }

            String stageName = StringArgumentType.getString(commandIn, "stageName");
            StageController.STAGES stage;
            try {
                stage = StageController.STAGES.valueOf(stageName);
            } catch (IllegalArgumentException e) {
                commandIn.getSource().sendFailure(Component.literal("Invalid stage name! Valid stage names are: {}" + StageController.getStageNames()));
                return 0;
            }

            NuclearWinter.stageController.activateStaging(player.level(), stage);
            player.sendSystemMessage(Component.literal("Stage set to " + stageName));

            return Command.SINGLE_SUCCESS;
        }
        commandIn.getSource().sendFailure(Component.literal("Command source is not a player! You must be a player to run this command!"));
        return 0;
    }

    private static int runCmdStatus(CommandContext<CommandSourceStack> commandIn) {
        if (commandIn.getSource().getEntity() instanceof Player player) {
            if (NuclearWinter.stageController.isLoadedStage(player.level())) {
                IStageLevelSettings stageSettings = CapabiltiesAttacher.getStageLevelSettings(player.level());
                StageBase loadedStage = NuclearWinter.stageController.getActiveStage(player.level());

                player.sendSystemMessage(Component.literal(getStageStatus(stageSettings, loadedStage)));
            } else {
                player.sendSystemMessage(Component.literal("Staging is not active!"));
            }
            return Command.SINGLE_SUCCESS;
        }
        commandIn.getSource().sendFailure(Component.literal("Command source is not a player! You must be a player to run this command!"));
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
            if (CapabiltiesAttacher.hasStageLevelSettings(player.level())) {
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

    private static int runCmdStart(CommandContext<CommandSourceStack> commandIn) {
        if (commandIn.getSource().getEntity() instanceof Player player) {
            if (NuclearWinter.stageController.isLoadedStage(player.level())) {
                commandIn.getSource().sendFailure(Component.literal("Stage is already loaded!"));
                return 0;
            }

            if (CapabiltiesAttacher.hasStageLevelSettings(player.level())) {
                IStageLevelSettings stageSettings = CapabiltiesAttacher.getStageLevelSettings(player.level());
                NuclearWinter.stageController.activateStaging(player.level(), stageSettings.getCurrentStage());
            } else {
                //TODO Should we set this to ApocLow?
                NuclearWinter.stageController.activateStaging(player.level(), StageController.STAGES.PREAPOC);
            }
            player.sendSystemMessage(Component.literal("Staging activated!"));
            return Command.SINGLE_SUCCESS;
        }
        commandIn.getSource().sendFailure(Component.literal("Command source is not a player! You must be a player to run this command!"));
        return 0;
    }
}
