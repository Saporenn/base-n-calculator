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
                root.then(Author.registerClient(root));
                root.then(Axolotl.registerClient(root));
                // root.then(Calc.registerShared(root));
                root.then(Convert.registerClient(root));
                root.then(ConvertFrom.registerClient(root));
                root.then(Help.registerClient(root));
                // root.then(Register.registerShared(root));
                root.then(Signal.registerClient(root));
                // root.then(Simplify.registerShared(root));
                // root.then(TruthTable.registerShared(root));

                dispatcher.register(root);
            });
        });
    }

}
