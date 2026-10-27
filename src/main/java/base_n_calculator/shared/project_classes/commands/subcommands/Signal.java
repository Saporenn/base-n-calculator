package base_n_calculator.shared.project_classes.commands.subcommands;

import base_n_calculator.shared.project_constants.NumericConstants;
import base_n_calculator.shared.project_classes.commands.util.ConversionUtil;
import base_n_calculator.shared.utils.BaseNUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class Signal {
    private final static String commandName = "signal";
    private final static String encodeSubcommand_name = "encode";
    private final static String encodeSubcommand_arg_1_name = "redstone_signal_strength";
    private final static String decodeSubcommand_name = "decode";
    private final static String decodeSubcommand_arg_1_name = "binary_redstone_signal_strength";

    // this can't fail, as brigadier confirms the input is within 0-15
    private static Component getEncodeLiteralComponent(CommandContext<?> context) {
        return Component.literal(">> Signal Encode Result: " +
                BaseNUtils.convert(10, context.getArgument(encodeSubcommand_arg_1_name, Integer.class).toString(), 2).finalString()
                + " (Base-2)");
    }

    private static Component getDecodeLiteralComponentOrThrow(CommandContext<?> context) throws CommandSyntaxException {
        return ConversionUtil.getConversionLiteralComponentOrThrow(
                2,
                context.getArgument(decodeSubcommand_arg_1_name, String.class),
                10,
                "Signal Decode Result"
        );
    }

    private static int executeEncodeShared(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> getEncodeLiteralComponent(context), false);
        return 1;
    }

    private static int executeEncodeClient(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(getEncodeLiteralComponent(context));
        return 1;
    }

    private static int executeDecodeShared(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> {
            try {
                return getDecodeLiteralComponentOrThrow(context);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, false);
        return 1;
    }

    private static int executeDecodeClient(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(getDecodeLiteralComponentOrThrow(context));
        return 1;
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> registerClient(LiteralArgumentBuilder<FabricClientCommandSource> command) {
        return command
                .then(ClientCommands.literal(Signal.commandName)
                        .then(ClientCommands.literal(Signal.encodeSubcommand_name)
                                .then(ClientCommands.argument(Signal.encodeSubcommand_arg_1_name, IntegerArgumentType.integer(NumericConstants.MIN_SIGNAL_STRENGTH, NumericConstants.MAX_SIGNAL_STRENGTH))
                                        .executes(Signal::executeEncodeClient)
                                )
                        )
                        .then(ClientCommands.literal(Signal.decodeSubcommand_name)
                                .then(ClientCommands.argument(decodeSubcommand_arg_1_name, IntegerArgumentType.integer(NumericConstants.MIN_SIGNAL_STRENGTH, NumericConstants.MAX_SIGNAL_STRENGTH))
                                        .executes(Signal::executeDecodeClient)
                                )
                        )
                );
    }

    public static LiteralArgumentBuilder<CommandSourceStack> registerShared(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command
                .then(Commands.literal(Signal.commandName)
                        .then(Commands.literal(Signal.encodeSubcommand_name)
                                .then(Commands.argument(Signal.decodeSubcommand_arg_1_name, StringArgumentType.string())
                                        .executes(Signal::executeEncodeShared)
                                )
                        )
                        .then(Commands.literal(Signal.decodeSubcommand_name)
                                .then(Commands.argument(Signal.decodeSubcommand_arg_1_name, StringArgumentType.string())
                                        .executes(Signal::executeDecodeShared)
                                )
                        )
                );
    }



}