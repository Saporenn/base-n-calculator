package base_n_calculator.shared.project_classes;

import base_n_calculator.shared.project_constants.BNCModuleState;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class BNCModule {
    private static String UNLABELED_MODULE = "UnlabledBNCModule";
    private static Optional<IllegalStateException> checkStateTransition(BNCModuleState requiredState, BNCModuleState targetState, BNCModule module) {
        if (module.currentState != requiredState) {
            return Optional.of(new IllegalStateException("Module '" + module.label
                    + "' must be in state '" + requiredState.name()
                    + "' to enter state '" + targetState.name()
                    + "'. Current state " + module.currentState)
            );
        }
        return Optional.empty();
    }

    private static RuntimeException getLifecycleException(String context, String moduleLabel, Exception e, String callbackLabel) {
        return new RuntimeException("Failed to " + context + " '"
                + moduleLabel + "'. Details: "
                + "\n-> Callback Label: '" + callbackLabel + "'"
                + "\n-> Exception: " +  e.getMessage()
        );
    }

    private static void runLifecycleCallbacks(BNCModule module, String lifecycleContext, Map<String, Consumer<Object>> callbacks, BNCModuleState resultState) {
        callbacks.forEach((callbackLabel, callback) -> {
            try {
                callback.accept(module.environmentLink);
            } catch (Exception e) {
                throw getLifecycleException(lifecycleContext, module.label, e, callbackLabel);
            }
        });
        module.currentState = resultState;
    }

    private final Set<BNCModuleKey<?>> dependencies = ConcurrentHashMap.newKeySet();
    private final String label;

    private final Map<String, Consumer<Object>> initCallbacks = new LinkedHashMap<>();
    private final Map<String, Consumer<Object>> startCallbacks = new LinkedHashMap<>();
    private final Map<String, Consumer<Object>> stopCallbacks = new LinkedHashMap<>();

    private BNCModuleState currentState = BNCModuleState.UNINITIALIZED;
    private BNCModuleEnvironmentLink environmentLink;
    private BNCModuleEnvironmentLink getEnvironmentLink() {
        return Objects.requireNonNull(
                environmentLink,
                "Module '" + this.label + "' attempts to call environment dependent methods whilst not being linked to an environment."
        );
    }

    public BNCModule() {
        this(UNLABELED_MODULE);
    }
    public BNCModule(String label) {
        this.label = Objects.requireNonNullElse(label, UNLABELED_MODULE);
    }

    protected void onInit (String callbackLabel, Consumer<Object> callback) {
        this.initCallbacks.put(callbackLabel, callback);
    }

    protected void onStart (String callbackLabel, Consumer<Object> callback) {
        this.startCallbacks.put(callbackLabel, callback);
    }

    protected void onStop (String callbackLabel, Consumer<Object> callback) {
        this.stopCallbacks.put(callbackLabel, callback);
    }

    public final void attachEnvironmentLink(BNCModuleEnvironmentLink newLink) {
        if (this.environmentLink != null) {
            throw new RuntimeException("");
        }
        this.environmentLink = newLink;
    }

    public final void init() throws RuntimeException {
        final Optional<IllegalStateException> ex = checkStateTransition(BNCModuleState.UNINITIALIZED, BNCModuleState.INITIALIZED, this);
        ex.ifPresent(e -> { throw e; });

        runLifecycleCallbacks(this, "init", initCallbacks, BNCModuleState.INITIALIZED);
    }

    public void start() throws RuntimeException {
        final Optional<IllegalStateException> ex = checkStateTransition(BNCModuleState.INITIALIZED, BNCModuleState.STARTED, this);
        ex.ifPresent(e -> { throw e; });

        runLifecycleCallbacks(this, "start",  startCallbacks, BNCModuleState.STARTED);
    }

    public void stop() throws RuntimeException {
        final Optional<IllegalStateException> ex = checkStateTransition(BNCModuleState.STARTED, BNCModuleState.STOPPED, this);
        ex.ifPresent(e -> { throw e; });

        runLifecycleCallbacks(this, "stop", stopCallbacks, BNCModuleState.STOPPED);
    }

    public BNCModuleState getCurrentState() {
        return this.currentState;
    }

    public Set<BNCModuleKey<?>> getDependencies() {
        return Set.copyOf(this.dependencies);
    }

    public String getLabel() {
        return this.label;
    }

    public <TModule extends BNCModule> TModule require(BNCModuleKey<TModule> moduleKey) {
        return this.getEnvironmentLink().require(this, moduleKey);
    }

    public <TModule extends BNCModule> TModule requireInitialized(BNCModuleKey<TModule> moduleKey) {
        return this.getEnvironmentLink().require(this, moduleKey, BNCModuleState.INITIALIZED);
    }

    public <TModule extends BNCModule> TModule requireStarted(BNCModuleKey<TModule> moduleKey) {
        return this.getEnvironmentLink().require(this, moduleKey, BNCModuleState.STARTED);
    }
}

