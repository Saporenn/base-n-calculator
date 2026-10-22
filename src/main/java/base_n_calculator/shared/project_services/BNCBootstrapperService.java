package base_n_calculator.shared.project_services;

import base_n_calculator.shared.project_classes.BNCModuleEnvironment;
import base_n_calculator.shared.project_constants.BNCModuleEnvironmentInjectionPolicy;
import base_n_calculator.shared.project_managers.RadixRepresentationRegistry;
import base_n_calculator.shared.project_managers.CommandRegistry;

public class BNCBootstrapperService {
    public static void boot() {
        final BNCModuleEnvironment env = new BNCModuleEnvironment(BNCModuleEnvironmentInjectionPolicy.BEFORE_START)
                .injectModule(RadixRepresentationRegistry.KEY)
                .injectModule(CommandRegistry.KEY);

        env.init().start();
    }
}
