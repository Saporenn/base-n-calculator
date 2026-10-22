package base_n_calculator.shared.project_managers;

import base_n_calculator.shared.project_classes.BNCModule;
import base_n_calculator.shared.project_classes.BNCModuleKey;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CommandRegistry extends BNCModule {
    public static final String LABEL = "COMMAND_REGISTRY";
    public static final BNCModuleKey<CommandRegistry> KEY
            = new BNCModuleKey<>(LABEL, CommandRegistry::new);

    // will refactor yes ik it's messy this is v0.5

    private void startClient() {
            ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
                dispatcher.register(ClientCommands.literal("bnc")
                        .then(ClientCommands.literal("author")
                                .executes(context -> {
                                    context.getSource().sendFeedback(Component.literal(">> @Saporen_"));
                                    return 1;
                                })
                        )
                        .then(ClientCommands.literal("help")
                                .executes(context -> {
                                    context.getSource().sendFeedback(Component.literal(
                                            """
                                                    >> Here's a full list of Base-N Calculator's Commands:\s
                                                    | /bnc author -> Shows the creator of the mod.\s
                                                    | /bnc help -> Shows this list.
                                                    | /bnc axolotl -> Greatness. (Aliases: /bnc axo, /bnc ajolote)
                                                    | /bnc convert -> Converts a number in base-n to another base. (Aliases: /bnc cv, /bnc conv, /convert )
                                                               |_<radix representation> -> <number> -> <radix representation>
                                                               |
                                                               |_<radix prefixed num> -> <radix representation>
                                                    \s"""));
                                    return 1;
                                })
                        )
                        .then(ClientCommands.literal("axolotl").executes(this::printAxoClient))
                        .then(ClientCommands.literal("axo").executes(this::printAxoClient))
                        .then(ClientCommands.literal("ajolote").executes(this::printAxoClient))
                );
            });
    }

    private void startServer() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, buildContext, selection) -> {
                    dispatcher.register(Commands.literal("bnc")
                            .then(Commands.literal("author")
                                    .executes(context -> {
                                        context.getSource().sendSuccess(
                                                () -> Component.literal(">> @Saporen_"),
                                                false
                                        );
                                        return 1;
                                    })
                            )
                            .then(Commands.literal("help")
                                    .executes(context -> {
                                        context.getSource().sendSuccess(
                                                () -> Component.literal(
                                                        ">> Base-N Calculator commands: " +
                                                                "/bnc author, /bnc help, " +
                                                                "/bnc axolotl"
                                                ),
                                                false
                                        );
                                        return 1;
                                    })
                            )
                            .then(Commands.literal("axolotl")
                                    .executes(this::printAxoServer)
                            )
                            .then(Commands.literal("axo")
                                    .executes(this::printAxoServer)
                            )
                            .then(Commands.literal("ajolote")
                                    .executes(this::printAxoServer)
                            )
                    );
                }
        );
    }


    private int printAxoServer(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal(">> ꒰(˶• ᴗ •˶)꒱"), false);
        return 1;
    }

    private int printAxoClient(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Component.literal(">> ꒰(˶• ᴗ •˶)꒱"));
        return 1;
    }

    private RadixRepresentationRegistry radixRepresentationRegistry ;

    public CommandRegistry() {
        super(LABEL);

        this.onInit("ModuleRequires", (_) -> {
            this.radixRepresentationRegistry = this.require(RadixRepresentationRegistry.KEY);
        });

        this.onStart("CommandRegistration", (_) -> {
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                startClient();
            } else {
                startServer();
            }
        });
    }

}
