package base_n_calculator.client.entry;

import base_n_calculator.shared.project_services.BNCBootstrapperService;
import net.fabricmc.api.ClientModInitializer;

public class Initializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BNCBootstrapperService.boot();
    }
}