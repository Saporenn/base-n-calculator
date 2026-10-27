package base_n_calculator.shared.project_classes.commands.subcommands;

import base_n_calculator.shared.project_classes.commands.suggestion_providers.RadixPrefixSuggestionProvider;
import base_n_calculator.shared.project_constants.NumericConstants;

import base_n_calculator.shared.project_classes.commands.util.ConversionUtil;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Convert {
    private static final String commandName = "convert";
    private static final String arg1Name = "radix_prefixed_number";
    private static final String arg2Name = "base_to_convert_to";

    private static int executeClient(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(ConversionUtil.tryConvert(context, arg1Name, arg2Name));
        return 1;
    }

    private static int executeShared(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> {
            try {
                return ConversionUtil.tryConvert(context, arg1Name, arg2Name);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, false);
        return 1;
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> registerClient(LiteralArgumentBuilder<FabricClientCommandSource> command) {
        return command
                .then(ClientCommands.literal(commandName)
                    .then(ClientCommands.argument(arg1Name, StringArgumentType.string())
                            .suggests(new RadixPrefixSuggestionProvider.Client())
                            .then(ClientCommands.argument(arg2Name, IntegerArgumentType.integer(NumericConstants.MIN_RADIX_AMOUNT, NumericConstants.MAX_RADIX_AMOUNT))
                                    .executes(Convert::executeClient)
                )));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> registerShared(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command
                .then(Commands.literal(commandName)
                        .then(Commands.argument(arg1Name, StringArgumentType.string())
                                .suggests(new RadixPrefixSuggestionProvider.Shared())
                                .then(Commands.argument(arg2Name, IntegerArgumentType.integer(NumericConstants.MIN_RADIX_AMOUNT, NumericConstants.MAX_RADIX_AMOUNT))
                                        .executes(Convert::executeShared)
                )));
    }
}