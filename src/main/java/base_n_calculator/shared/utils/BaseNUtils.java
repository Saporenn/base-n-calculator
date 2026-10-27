package base_n_calculator.shared.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BaseNUtils {
    public interface ConversionResult {
        boolean success();
        String inputtedString();
        String finalString();
        char firstInvalidChar();
        int firstInvalidIndex();
    }

    private static final class CR implements ConversionResult {
        final boolean success;
        final String inputtedString;
        final String finalString;
        final char firstInvalidChar;
        final int firstInvalidIndex;

        CR(boolean success, String inputtedString, String finalString, char firstInvalidChar, int firstInvalidIndex) {
            this.success = success;
            this.inputtedString = inputtedString;
            this.finalString = finalString;
            this.firstInvalidChar = firstInvalidChar;
            this.firstInvalidIndex = firstInvalidIndex;
        }

        public boolean success() {return this.success;}
        public String inputtedString() {return this.inputtedString;}
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

    private static String convertBase10ToBaseN(BigInteger base10Integer, BigInteger baseToTranslateTo, String translationRadixAlphabet) {
        if (base10Integer.equals(BigInteger.ZERO)) {
            return String.valueOf(translationRadixAlphabet.charAt(0));
        }

        StringBuilder result = new StringBuilder();

        while (base10Integer.compareTo(BigInteger.valueOf(0)) > 0) {
            BigInteger remainder = base10Integer.mod(baseToTranslateTo);
            result.append(translationRadixAlphabet.charAt(remainder.intValue()));

            base10Integer = new BigDecimal(base10Integer)
                    .divide(new BigDecimal(baseToTranslateTo), 0, RoundingMode.FLOOR)
                    .toBigInteger();
        }

        return result
                .reverse()
                .toString();
    }

    public static ConversionResult convert(int startingRadix, String startingIntString, int baseToTranslateTo) {
        final String startingRadixAlphabet = getAlphabetOfRadix(startingRadix);
        final String translationRadixAlphabet = getAlphabetOfRadix(baseToTranslateTo);

        BigInteger integerValueInDecimal = new BigInteger("0");

        if (startingRadix == 1) {
            for (int i = 0; i < startingIntString.length(); i++) {
                if (startingIntString.charAt(i) != '1') return
                new CR(
                        false,
                        startingIntString,
                        INVALID_STRING,
                        startingIntString.charAt(i),
                        startingIntString.length() - i - 1
                );
            }

            // minecraft strings max out at 254 characters
            integerValueInDecimal = BigInteger.valueOf(startingIntString.length());
        } else {
            for (int position = 0; position < startingIntString.length(); position++) {
                int normalizedStringPosition = startingIntString.length() - position - 1; // we read from lsb
                int charValue = startingRadixAlphabet.indexOf(startingIntString.charAt(normalizedStringPosition)); // no we shall not be storing each value in a map

                if (charValue == -1) {
                    return
                        new CR(
                        false,
                                startingIntString,
                                INVALID_STRING,
                                startingIntString.charAt(normalizedStringPosition),
                                normalizedStringPosition
                        );
                }
                BigInteger radix = BigInteger.valueOf(startingRadix);
                BigInteger baseValue = BigInteger.valueOf(charValue);

                BigInteger positionalDigitValue = radix.pow(position);
                BigInteger resultingDigitValue = baseValue.multiply(positionalDigitValue);

                integerValueInDecimal = integerValueInDecimal.add(resultingDigitValue);
            }
        }

        if (baseToTranslateTo == 10) {
            return new CR(
                    true,
                    startingIntString,
                    integerValueInDecimal.toString(),
                    ' ',
                    -1
            );
        }

        return new CR(
                true,
                startingIntString,
                convertBase10ToBaseN(integerValueInDecimal, BigInteger.valueOf(baseToTranslateTo), translationRadixAlphabet),
                ' ',
                -1
        );
    }
}
