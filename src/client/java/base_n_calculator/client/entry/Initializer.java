package base_n_calculator.client.entry;

import base_n_calculator.client.project_managers.ClientCommandRegistry;
import base_n_calculator.shared.project_classes.BNCModuleEnvironment;
import net.fabricmc.api.ClientModInitializer;

public class Initializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        final BNCModuleEnvironment env = new BNCModuleEnvironment()
                .injectModule(ClientCommandRegistry.KEY)
                .init()
                .start();
    }
}