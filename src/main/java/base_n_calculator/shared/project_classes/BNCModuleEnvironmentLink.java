package base_n_calculator.shared.project_classes;

import base_n_calculator.shared.project_constants.BNCModuleState;

public class BNCModuleEnvironmentLink {
    private static IllegalStateException nullModuleException(BNCModule requirer, BNCModuleKey<?> moduleKey) {
        return new IllegalStateException("Module '" + requirer.getLabel()
                + "' attempts to hard require instance of ModuleKey '" + moduleKey.getName()
                + "' in it's environment, but it doesn't exist.");
    }

    private static IllegalStateException invalidModuleStateException(BNCModule requirer, BNCModuleKey<?> moduleKey, BNCModuleState requiredState, BNCModule module) {
        return new IllegalStateException("Module '" + requirer.getLabel()
                + "' attempts to hard require instance of ModuleKey '" + moduleKey.getName()
                + "' in state '"  + requiredState.name()
                + "', but the existing instance has not met the state. \nCurrent state: " + module.getCurrentState().name());
    }

    private final BNCModuleEnvironment environment;
    public BNCModuleEnvironmentLink(BNCModuleEnvironment env) {
        this.environment = env;
    }

    public <TModule extends BNCModule> TModule require(BNCModule requirer, BNCModuleKey<TModule> moduleKey) {
        return require(requirer, moduleKey, null);
    }
    @SuppressWarnings("unchecked")
    public <TModule extends BNCModule> TModule require(BNCModule requirer, BNCModuleKey<TModule> moduleKey, BNCModuleState requiredState) {
        if (!this.environment.hasInstanceOfModuleKey(moduleKey)) {
            throw nullModuleException(requirer, moduleKey);
        }

        final TModule requiredModule = this.environment.getModule(moduleKey);
        if (requiredState != null && requiredModule.getCurrentState() != requiredState) {
            throw invalidModuleStateException(requirer, moduleKey, requiredState, requiredModule);
        }

        return requiredModule;
    }
}
