package base_n_calculator.shared.project_managers;

import base_n_calculator.shared.project_classes.BNCModule;
import base_n_calculator.shared.project_classes.BNCModuleKey;
import base_n_calculator.shared.project_classes.RadixRepresentation;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RadixRepresentationRegistry extends BNCModule {
    public static String LABEL = "RADIX_REPRESENTATION_REGISTRY";
    public static final BNCModuleKey<RadixRepresentationRegistry> KEY
            = new BNCModuleKey<>(RadixRepresentationRegistry.LABEL, RadixRepresentationRegistry::new);

    private final Map<String, Integer> aliasToRadixMap = new ConcurrentHashMap<>();
    private final Map<Integer, Set<String>> radixToAliasMap = new ConcurrentHashMap<>();

    public RadixRepresentationRegistry() {
        super(LABEL);

        this.onInit("DefaultBaseRepresentationRegisters", (_) -> {
            this.add(new RadixRepresentation(2, "binary", "bin", "base2"));
            this.add(new RadixRepresentation(8, "octal", "oct", "base8"));
            this.add(new RadixRepresentation(10, "decimal", "dec", "base10"));
            this.add(new RadixRepresentation(16, "hexadecimal", "hex", "base16"));
            this.add(new RadixRepresentation(64, "base64"));
        });
    }

    public RadixRepresentationRegistry add(RadixRepresentation... reps) {
        for (RadixRepresentation rep : reps) {
            int radix = rep.getRadix();

            for (String alias : rep.getAliases()) {
                Integer oldRadix = aliasToRadixMap.put(alias, radix);

                if (oldRadix != null && oldRadix != radix) {
                    Set<String> oldAliases = radixToAliasMap.get(oldRadix);
                    if (oldAliases != null) {
                        oldAliases.remove(alias);

                        if (oldAliases.isEmpty()) radixToAliasMap.remove(oldRadix);
                    }
                }

                radixToAliasMap.computeIfAbsent(radix, k -> ConcurrentHashMap.newKeySet()).add(alias);
            }
        }
        return this;
    }

    public RadixRepresentationRegistry removeByRadix(int... radices) {
        for (int radix : radices) {
            final Set<String> aliasesToRemove = this.radixToAliasMap.get(radix);
            if (aliasesToRemove == null) continue;

            aliasesToRemove.forEach(this.aliasToRadixMap::remove);
            this.radixToAliasMap.remove(radix);
        }
        return this;
    }

    public RadixRepresentationRegistry removeByAlias(String... aliases) {
        for (String alias : aliases) {
            final Integer radixAssociatedWithAlias = this.aliasToRadixMap.get(alias);
            if (radixAssociatedWithAlias == null) continue;
            this.removeByRadix(radixAssociatedWithAlias);
        }

        return this;
    }

    public RadixRepresentationRegistry removeAlias(String... aliases) {
        for (String alias : aliases) {
            final Integer radixAssociatedWithAlias = this.aliasToRadixMap.get(alias);
            if (radixAssociatedWithAlias == null) continue;

            this.aliasToRadixMap.remove(alias);

            final Set<String> listToRemoveAliasFrom = this.radixToAliasMap.get(radixAssociatedWithAlias);
            if (listToRemoveAliasFrom != null) {
                listToRemoveAliasFrom.remove(alias);

                if (listToRemoveAliasFrom.isEmpty()) {
                    this.radixToAliasMap.remove(radixAssociatedWithAlias);
                }
            }
        }
        return this;
    }

    public Optional<Integer> getRadixFromAlias(String alias) {
        return Optional.ofNullable(this.aliasToRadixMap.get(alias));
    }

    public Optional<Set<String>> getAliasesFromRadix(int radix) {
        return Optional.ofNullable(this.radixToAliasMap.get(radix));
    }

    public Set<Integer> getAllRadices() {
        return this.radixToAliasMap.keySet();
    }

    public Set<String> getAllAliases() {
        return this.aliasToRadixMap.keySet();
    }
}