package base_n_calculator.shared.project_classes.commands.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class Help {
    private final static String commandName = "help";

    private static Component getLiteralComponent() {
        return Component.literal(
                """
                        >> Here's a full list of Base-N Calculator's Commands:
                        | /bnc author -> Shows the creator of the mod.
                        | /bnc help -> Shows this list.
                        | /bnc axolotl -> Greatness. (Aliases: /bnc axo, /bnc ajolote)
                        | /bnc convert <radix prefixed num>, <radix representation> -> Converts a radix-prefixed number in base-n to another base. (Aliases: /bnc cv, /bnc conv)
                        | /bnc convert_from <radix representation>, <number>, <radix representation> -> Converts a number in base-n to another base. (Aliases: /bnc cv_from, /bnc conv_from)
                        | /bnc signal encode <redstone_signal_strength> -> Converts a redstone signal strength (0-15) to its binary representation.
                        | /bnc signal decode <binary_signal_strength> -> Converts a binary string to a redstone signal strength (0-15).
                        """);
    }

    private static int executeShared(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(Help::getLiteralComponent, false);
        return 1;
    }

    private static int executeClient(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Help.getLiteralComponent());
        return 1;
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> registerClient(LiteralArgumentBuilder<FabricClientCommandSource> command) {
        return command
                .then(ClientCommands.literal(Help.commandName)
                .executes(Help::executeClient));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> registerShared(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command
                .then(Commands.literal(Help.commandName)
                .executes(Help::executeShared));
    }
}
