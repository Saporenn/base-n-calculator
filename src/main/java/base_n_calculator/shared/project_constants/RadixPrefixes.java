package base_n_calculator.shared.project_constants;

import java.util.HashMap;
import java.util.Map;

public class RadixPrefixes {
    private static Map<String, Integer> makePrefixesMap() {
        HashMap<String, Integer> map = HashMap.newHashMap(NumericConstants.MAX_RADIX_AMOUNT + 4);
        map.put("0b", 2);
        map.put("0o", 8);
        map.put("0d", 10);
        map.put("0x", 16);

        for (int i = NumericConstants.MIN_RADIX_AMOUNT; i <= NumericConstants.MAX_RADIX_AMOUNT ; i++) {
            map.put(i + ";", i);
        }

        return map;
    }

    private static Map<String, Integer> prefixMap;

    public static Map<String, Integer> getPrefixToRadixMap() {
        if (prefixMap == null) { prefixMap = makePrefixesMap(); }
        return prefixMap;
    }
}
