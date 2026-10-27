package base_n_calculator.shared.project_classes.commands.subcommands;

import base_n_calculator.shared.project_classes.BNCSubcommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class Author implements BNCSubcommand {
    private static final String commandName = "axolotl";

    private static Component getLiteralComponent() {
        return Component.literal(">> @Saporenn on Github");
    }

    private static int executeShared(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(Author::getLiteralComponent, false);
        return 1;
    }

    private static int executeClient(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Author.getLiteralComponent());
        return 1;
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> registerClient(LiteralArgumentBuilder<FabricClientCommandSource> command) {
        return command
                .then(ClientCommands.literal(Author.commandName)
                        .executes(Author::executeClient));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> registerShared(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command
                .then(Commands.literal(Author.commandName)
                        .executes(Author::executeShared));
    }
}
