package base_n_calculator.shared.project_classes;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class BNCModuleKey<TBNCModuleType extends BNCModule> {
    private final String name;
    private final Supplier<TBNCModuleType> factory;

    public BNCModuleKey(String name, Supplier<TBNCModuleType> factory) {
        this.name = Objects.requireNonNull(name, "name");
        this.factory = Objects.requireNonNull(factory, "factory");
    }

    public String getName() {
        return name;
    }

    public Supplier<TBNCModuleType> getFactory() {
        return this.factory;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return ((BNCModuleKey<?>) obj).factory == this.factory && ((BNCModuleKey<?>) obj).name.equals(this.name);
    }
}
