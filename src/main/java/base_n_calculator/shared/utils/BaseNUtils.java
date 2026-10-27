package base_n_calculator.shared.utils;

public class BaseNUtils {
    public interface ConversionResult {
        boolean success();
        String finalString();
        char firstInvalidChar();
        int firstInvalidIndex();
    }

    private static final class CR implements ConversionResult {
        final boolean success;
        final String finalString;
        final char firstInvalidChar;
        final int firstInvalidIndex;

        CR(boolean success, String finalString, char firstInvalidChar, int firstInvalidIndex) {
            this.success = success;
            this.finalString = finalString;
            this.firstInvalidChar = firstInvalidChar;
            this.firstInvalidIndex = firstInvalidIndex;
        }

        public boolean success() {return this.success;}
        public String finalString() {return this.finalString;}
        public char firstInvalidChar() {return this.firstInvalidChar;}
        public int firstInvalidIndex() {return this.firstInvalidIndex;}

    }

    public static class ALPHABET_FAMILIES {
        public static final String BASE1_THROUGH_36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final String BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        public static final String BASE37_THROUGH_62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        public static final String BASE63_AND_64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    }


    private static final String INVALID_STRING = "INVALID_STRING";

    private static boolean inRangeInclusive(int x, int a, int b) {
        return x >= a && x <= b;
    }
    
    private static String getAlphabetOfRadix(int radix) {
        if (radix == 1) {
            return "1";
        }

        if (radix == 32) {
            return ALPHABET_FAMILIES.BASE32;
        }

        if (inRangeInclusive(radix, 1, 36)) {
            return ALPHABET_FAMILIES.BASE1_THROUGH_36.substring(0, radix);
        }

        if (inRangeInclusive(radix, 37, 62)) {
            return ALPHABET_FAMILIES.BASE37_THROUGH_62.substring(0, radix);
        }

        return ALPHABET_FAMILIES.BASE63_AND_64.substring(0, radix);
    }

    private static String convertBase10ToBaseN(int base10Integer, int baseToTranslateTo, String translationRadixAlphabet) {
        if (base10Integer == 0) {
            return String.valueOf(translationRadixAlphabet.charAt(0));
        }

        StringBuilder result = new StringBuilder();

        while (base10Integer > 0) {
            int remainder = base10Integer % baseToTranslateTo;
            result.append(translationRadixAlphabet.charAt(remainder));

            base10Integer = Math.floorDiv(base10Integer, baseToTranslateTo);
        }

        return result.reverse().toString();
    }

    public static ConversionResult convert(int startingRadix, String startingIntString, int baseToTranslateTo) {
        final String startingRadixAlphabet = getAlphabetOfRadix(startingRadix);
        final String translationRadixAlphabet = getAlphabetOfRadix(baseToTranslateTo);

        int integerValueInDecimal = 0;

        if (startingRadix == 1) {
            for (int i = 0; i < startingIntString.length(); i++) {
                if (startingIntString.charAt(i) != '1') return
                new CR(
                        false,
                        INVALID_STRING,
                        startingIntString.charAt(i),
                        startingIntString.length() - i - 1
                );
            }

            integerValueInDecimal = startingIntString.length();
        } else {
            for (int position = 0; position < startingIntString.length(); position++) {
                int normalizedStringPosition = startingIntString.length() - position - 1; // we read from lsb
                int charValue = startingRadixAlphabet.indexOf(startingIntString.charAt(normalizedStringPosition)); // no we shall not be storing each value in a map

                if (charValue == -1) {
                    return
                        new CR(
                        false,
                                INVALID_STRING,
                                startingIntString.charAt(normalizedStringPosition),
                                normalizedStringPosition
                        );
                }

                integerValueInDecimal += (int) (charValue * Math.pow(startingRadix, position));
            }
        }

        if (baseToTranslateTo == 10) {
            return new CR(true, Integer.toString(integerValueInDecimal), ' ', -1);
        }

        return new CR(
                true,
                convertBase10ToBaseN(integerValueInDecimal, baseToTranslateTo, translationRadixAlphabet),
                ' ',
                -1
        );
    }
}
