package base_n_calculator.shared.entry;

import base_n_calculator.shared.project_classes.BNCModuleEnvironment;
import base_n_calculator.shared.project_managers.BooleanExpressionRegistry;
import base_n_calculator.shared.project_managers.SharedCommandRegistry;
import net.minecraft.resources.Identifier;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Initializer implements ModInitializer {
    public static final String MOD_ID = "base-n-calculator";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        final BNCModuleEnvironment env = new BNCModuleEnvironment()
                .injectModule(SharedCommandRegistry.KEY)
                .init()
                .start();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}