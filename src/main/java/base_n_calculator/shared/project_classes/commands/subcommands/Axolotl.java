package base_n_calculator.shared.project_classes.commands.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class Axolotl {
    private static final String commandName = "axolotl";

    private static Component getLiteralComponent() {
        return Component.literal(">> ꒰(˶• ᴗ •˶)꒱");
    }

    private static int executeShared(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(Axolotl::getLiteralComponent, false);
        return 1;
    }

    private static int executeClient(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Axolotl.getLiteralComponent());
        return 1;
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> registerClient(LiteralArgumentBuilder<FabricClientCommandSource> command) {
        return command
                .then(ClientCommands.literal(Axolotl.commandName)
                .executes(Axolotl::executeClient));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> registerShared(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command
                .then(Commands.literal(Axolotl.commandName)
                .executes(Axolotl::executeShared));
    }
}
