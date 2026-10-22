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
}
