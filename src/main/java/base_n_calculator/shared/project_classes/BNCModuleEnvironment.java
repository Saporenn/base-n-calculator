package base_n_calculator.shared.project_classes;

import base_n_calculator.shared.project_constants.BNCModuleEnvironmentInjectionPolicy;
import base_n_calculator.shared.project_constants.BNCModuleRequirePolicy;
import base_n_calculator.shared.project_constants.BNCModuleState;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BNCModuleEnvironment {

    public interface ModuleInjectabilityStatusResult {
        boolean canInject();
        BNCModuleState currentState();
        BNCModuleState edgeState();
    }
    private record MISR(boolean canInject, BNCModuleState edgeState, BNCModuleState currentState) implements ModuleInjectabilityStatusResult {}



    /*
        As of now, there is no lazy module initialization implemented yet at the module level (modules cannot dynamically create other ones in its same environment), so this can be kept simple.
        But in the future, if/when it's implemented on the environmentLinker -> module method, this function should automatically recognize cyclic lifecycle dependencies between modules that lazyRequire each other.
        This can be accomplished by adding a transitional state enum called `BNCLifecycleTransitionalState`, which allows the linker to implicitly store when Module A requires Module B.
        If both modules lazily require each other in the same transitional stage, there is a cyclic dependency at that given lifecycle state.

        Another problem lies in the fact that lazyRequire will force Module B to be added to Module A's environment.
        If this is done during a lifecycle transition, then moduleInstances Map will be corrupted, causing a bug in the loop below.
        A queue will likely need to be made here and exposed to the linker which will allow lazyRequire to push "late" modules to be processed and added to moduleInstances sequentially.
        For now, it's highly probable that this would be the refactor required for safety purposes if you ever intended to add this.
    */
    private void processModules(Predicate<BNCModule> shouldProcess, Consumer<BNCModule> lifecycleAction, BNCModuleState newState) {
        for (BNCModule module : this.moduleInstances.values()) {
            if (module != null && shouldProcess.test(module)) {
                lifecycleAction.accept(module);
            }
        }

        this.state = newState;
    }

    private IllegalStateException invalidTransitionException(BNCModuleState requestedState, BNCModuleState requiredState, BNCModuleState currentState) {
        return new IllegalStateException("Environment cannot transition from its current state '" + currentState.name()
                + "' to the requested state '" + requestedState.name() + "'"
                + "\n Required state: '" + requestedState.name() + "'"
        );
    }

    private final Map<BNCModuleKey<?>, BNCModule> moduleInstances = new HashMap<>();
    private final BNCModuleEnvironmentInjectionPolicy injectionPolicy;
    private final BNCModuleRequirePolicy requirePolicy;
    private final BNCModuleEnvironmentLink environmentLink;
    private BNCModuleState state;

    public BNCModuleEnvironment(BNCModuleRequirePolicy requirePolicy) {
        this(BNCModuleEnvironmentInjectionPolicy.BEFORE_INIT, requirePolicy);
    }
    public BNCModuleEnvironment(BNCModuleEnvironmentInjectionPolicy injectionPolicy) {
        this(injectionPolicy, BNCModuleRequirePolicy.EXPLICIT_REGISTER);
    }
    public BNCModuleEnvironment() {
        this(BNCModuleEnvironmentInjectionPolicy.BEFORE_INIT, BNCModuleRequirePolicy.EXPLICIT_REGISTER);
    }
    public BNCModuleEnvironment(BNCModuleEnvironmentInjectionPolicy injectionPolicy, BNCModuleRequirePolicy requirePolicy) {
        this.injectionPolicy = injectionPolicy;
        this.requirePolicy = requirePolicy;
        this.environmentLink = new BNCModuleEnvironmentLink(this);
        this.state = BNCModuleState.UNINITIALIZED;
    }

    public BNCModuleEnvironment init() {
        if (this.state != BNCModuleState.UNINITIALIZED) {
            throw invalidTransitionException(BNCModuleState.INITIALIZED, BNCModuleState.UNINITIALIZED, this.state);
        }

        this.processModules(
                module -> module.getCurrentState() == BNCModuleState.UNINITIALIZED,
                BNCModule::init,
                BNCModuleState.INITIALIZED
        );
        return this;
    }

    public BNCModuleEnvironment start() {
        if (this.state != BNCModuleState.INITIALIZED) {
            throw invalidTransitionException(BNCModuleState.STARTED, BNCModuleState.INITIALIZED, this.state);
        }

        this.processModules(
                module -> module.getCurrentState() == BNCModuleState.INITIALIZED,
                BNCModule::start,
                BNCModuleState.STARTED
        );
        return this;
    }

    public BNCModuleEnvironment stop() {
        if (this.state != BNCModuleState.STARTED) {
            throw invalidTransitionException(BNCModuleState.STOPPED, BNCModuleState.STARTED, this.state);
        }

        this.processModules(
                module -> module.getCurrentState() == BNCModuleState.STARTED,
                BNCModule::stop,
                BNCModuleState.STOPPED
        );
        return this;
    }

    public BNCModuleEnvironment injectModule(BNCModuleKey<?> moduleKey) {
        ModuleInjectabilityStatusResult injectabilityStatusResult = this.checkModuleInjectabilityStatus();

        if (!injectabilityStatusResult.canInject()) {
            throw new RuntimeException("Environment cannot add modules in it's current state.");
        }

        BNCModule module = moduleKey.getFactory().get();
        module.attachEnvironmentLink(this.environmentLink);
        this.moduleInstances.put(moduleKey, module);
        return this;
    }

    public ModuleInjectabilityStatusResult checkModuleInjectabilityStatus() {
        switch (this.injectionPolicy) {
            case BEFORE_INIT -> {
                return new MISR(
                        this.state == BNCModuleState.UNINITIALIZED,
                        BNCModuleState.INITIALIZED,
                        this.state
                );
            }

            case BEFORE_START -> {
                return new MISR(
                        this.state != BNCModuleState.STARTED && this.state != BNCModuleState.STOPPED,
                        BNCModuleState.STARTED,
                        this.state
                );
            }

            case BEFORE_STOP -> {
                return new MISR(
                        this.state != BNCModuleState.STOPPED,
                        BNCModuleState.STOPPED,
                        this.state
                );
            }

            default -> { // case will never happen, but linter complains
                return new MISR(false, null, null);
            }
        }
    }

    public boolean hasInstanceOfModuleKey(BNCModuleKey<?> moduleKey) {
        return this.moduleInstances.containsKey(moduleKey);
    }

    @SuppressWarnings("unchecked")
    public <TModule extends BNCModule> TModule getModule(BNCModuleKey<TModule> moduleKey) {
        return (TModule) this.moduleInstances.get((moduleKey));
    }

    public Collection<BNCModule> getModules() {
        return this.moduleInstances.values();
    }

    public boolean hasModule(BNCModule module) {
        return this.moduleInstances.containsValue(module);
    }

    public BNCModuleRequirePolicy getRequirePolicy() {
        return this.requirePolicy;
    }

    public BNCModuleState getState() { return this.state; }
}
