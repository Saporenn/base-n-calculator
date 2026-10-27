package base_n_calculator.shared.project_managers;

import base_n_calculator.shared.project_classes.BNCModule;
import base_n_calculator.shared.project_classes.BNCModuleKey;
import base_n_calculator.shared.project_classes.commands.subcommands.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;

public class SharedCommandRegistry extends BNCModule {
    public static final String LABEL = "SHARED_COMMAND_REGISTRY";
    public static final BNCModuleKey<SharedCommandRegistry> KEY
            = new BNCModuleKey<>(LABEL, SharedCommandRegistry::new);

    public SharedCommandRegistry() {
        super(LABEL);

        this.onInit("ModuleRequires", (_) -> {

        });

        this.onStart("CommandRegistration", (_) -> {
            CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> {
                var root = Commands.literal("bnc");
                Author.registerShared(root);
                Axolotl.registerShared(root);
                // Calc.registerShared(root);
                Convert.registerShared(root);
                ConvertFrom.registerShared(root);
                Help.registerShared(root);
                // Register.registerShared(root);
                Signal.registerShared(root);
                // Simplify.registerShared(root);
                // TruthTable.registerShared(root);

                dispatcher.register(root);
            });
        });
    }

}
