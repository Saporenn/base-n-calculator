package base_n_calculator.shared.utils;

public final class StringUtils {
    private StringUtils() {
        throw new AssertionError("Library");
    }

    private static void addUnderscoreIfNeeded(StringBuilder builder) {
        if (!builder.isEmpty() && builder.charAt(builder.length() - 1) == '_') {
            builder.append('_');
        }
    }

    public static boolean isCharInRange(char target, char charMin, char charMax) {
        return target >= charMin && target <= charMax;
    }

    public static String toSnakeCase(String str) {
        StringBuilder formattedName = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);

            if (currentChar == ' ' || currentChar == '-' || currentChar == '_') {
                addUnderscoreIfNeeded(formattedName);
                continue;
            }

            if (isCharInRange(currentChar, 'a', 'z') || isCharInRange(currentChar, '0', '9')) {
                formattedName.append(currentChar);
                continue;
            }

            if (isCharInRange(currentChar, 'A', 'Z')) {
                addUnderscoreIfNeeded(formattedName);
                formattedName.append((char) (currentChar + 32));
            }
        }

        return formattedName.toString();
    }

    public static String toNth(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("StringUtils.toNth expects a positive integer");
        }

        String numericString = Integer.toString(num);
        int len = numericString.length();

        if (len > 1 && numericString.charAt(len - 2) == '1') {
            return numericString + "th"; // teens
        }

        return switch (numericString.charAt(len - 1)) {
            case ('1') -> numericString + "st";
            case ('2') -> numericString + "nd";
            case ('3') -> numericString + "rd";
            default -> numericString + "th";
        };
    }
}
