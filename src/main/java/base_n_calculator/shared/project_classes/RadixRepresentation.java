package base_n_calculator.shared.project_classes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RadixRepresentation {
    private final int radix;
    private final Set<String> aliases;

    public RadixRepresentation(int radix, List<String> aliases) {
        if (radix <= 0 || radix > 64) {
            throw new IllegalArgumentException(
                    "Radix must be a positive integer in range [1, 64]"
            );
        }
        this.radix = radix;
        this.aliases = ConcurrentHashMap.newKeySet();
        this.aliases.addAll(aliases);
    }

    public RadixRepresentation(int radix, String... aliases) {
        this(radix, List.of(aliases));
    }

    public RadixRepresentation(int radix, Set<String> aliases) {
        this(radix, aliases.stream().toList());
    }

    public int getRadix() {
        return this.radix;
    }

    public Set<String> getAliases() {
        return new HashSet<>(this.aliases);
    }

    public RadixRepresentation addAliases(List<String> aliases) {
        this.aliases.addAll(aliases);
        return this;
    }
    public RadixRepresentation addAliases(String... aliases) {
        this.aliases.addAll(List.of(aliases));
        return this;
    }

    public RadixRepresentation removeAliases(String... aliases) {
        List.of(aliases).forEach(this.aliases::remove);
        return this;
    }

    public boolean hasAliases(String... aliases) {
        return this.aliases.containsAll(List.of(aliases));
    }
}
