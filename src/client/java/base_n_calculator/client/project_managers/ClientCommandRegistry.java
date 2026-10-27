package base_n_calculator.client.project_managers;

import base_n_calculator.shared.project_classes.BNCModule;
import base_n_calculator.shared.project_classes.BNCModuleKey;
import base_n_calculator.shared.project_classes.commands.subcommands.*;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;

public class ClientCommandRegistry extends BNCModule {
    public static final String LABEL = "CLIENT_COMMAND_REGISTRY";
    public static final BNCModuleKey<ClientCommandRegistry> KEY
            = new BNCModuleKey<>(LABEL, ClientCommandRegistry::new);

    public ClientCommandRegistry() {
        super(LABEL);

        this.onInit("ModuleRequires", (_) -> {

        });

        this.onStart("CommandRegistration", (_) -> {
            ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
                var root = ClientCommands.literal("bnc");
                Author.registerClient(root);
                Axolotl.registerClient(root);
                // Calc.registerClient(root);
                Convert.registerClient(root);
                ConvertFrom.registerClient(root);
                Help.registerClient(root);
                // Register.registerClient(root);
                Signal.registerClient(root);
                // Simplify.registerClient(root);
                // TruthTable.registerClient(root);

                dispatcher.register(root);
            });
        });
    }

}
