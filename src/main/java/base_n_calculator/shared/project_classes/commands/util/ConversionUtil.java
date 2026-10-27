package base_n_calculator.shared.project_classes.commands.util;

import base_n_calculator.shared.project_constants.RadixPrefixes;
import base_n_calculator.shared.utils.BaseNUtils;
import base_n_calculator.shared.utils.BaseNUtils.ConversionResult;

import base_n_calculator.shared.utils.StringUtils;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic4CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.network.chat.Component;

import java.util.Map;

public class ConversionUtil {

    private static final DynamicCommandExceptionType RADIX_PREFIXED_NUM_TOO_SHORT = new DynamicCommandExceptionType(
            stringLen -> Component.literal("The provided number argument is too short (less than 3)! Length received: " + stringLen.toString())
    );

    private static final DynamicCommandExceptionType RADIX_PREFIXED_NUM_UNKNOWN_PREFIX = new DynamicCommandExceptionType(
            prefix -> Component.literal("The provided prefix '" + prefix + "' is an unknown radix prefix.")
    );

    private static final Dynamic2CommandExceptionType RADIXES_ARE_EQUIVALENT = new Dynamic2CommandExceptionType(
            (prefix, base) -> Component.literal("The provided prefix '" + prefix + "' already maps to base " + base + ".")
    );

    private static final DynamicCommandExceptionType RADIXES_ARE_EQUIVALENT_NO_PREFIX = new DynamicCommandExceptionType(
            (base) -> Component.literal("The provided bases (" + base + ") are already equivalent.")
    );

    private static final Dynamic4CommandExceptionType INVALID_STRING = new Dynamic4CommandExceptionType(
            (invalidString, base, invalidCharacter, invalidIndex) -> Component.literal("The provided string '" + invalidString + "' has an invalid character '" + invalidCharacter + "' for it's base ("+ base + ") at the " + StringUtils.toNth((int) invalidIndex + 1) + " character.")
    );

    public static Component getConversionLiteralComponentOrThrow(int baseToConvertFrom, String integerString, int baseToConvertTo, String resultMessage) throws CommandSyntaxException {
        ConversionResult conversionResult = BaseNUtils.convert(baseToConvertFrom, integerString, baseToConvertTo);
        if (!conversionResult.success()) {
            throw INVALID_STRING.create(conversionResult.finalString(), baseToConvertTo, conversionResult.firstInvalidChar(), conversionResult.firstInvalidIndex());
        }

        return Component.literal(">> "+ resultMessage + ": "+ conversionResult.finalString() + "(Base-" + baseToConvertTo + ")" );
    }

    public static Component tryConvert(CommandContext<?> context, String arg1Name, String arg2Name) throws CommandSyntaxException {
        String prefixedIntegerString = context.getArgument(arg1Name, String.class);
        int baseToConvertTo = context.getArgument(arg2Name, Integer.class);

        Map<String, Integer> validPrefixes = RadixPrefixes.getPrefixToRadixMap();

        if (prefixedIntegerString.length() < 3) {
            throw RADIX_PREFIXED_NUM_TOO_SHORT.create(prefixedIntegerString.length());
        }

        String inputtedPrefix = prefixedIntegerString.substring(0, 2);
        if (!validPrefixes.containsKey(inputtedPrefix)) {
            throw RADIX_PREFIXED_NUM_UNKNOWN_PREFIX.create(inputtedPrefix);
        }

        int baseToConvertFrom = validPrefixes.get(inputtedPrefix);
        if (baseToConvertFrom == baseToConvertTo) {
            throw RADIXES_ARE_EQUIVALENT.create(inputtedPrefix, baseToConvertTo);
        }

        return getConversionLiteralComponentOrThrow(baseToConvertFrom, prefixedIntegerString.substring(2), baseToConvertTo, "Conversion Result");
    }

    public static Component tryConvertFrom(CommandContext<?> context, String arg1Name, String arg2Name, String arg3Name) throws  CommandSyntaxException {
        int baseToConvertFrom = context.getArgument(arg1Name, Integer.class);
        String integerString = context.getArgument(arg2Name, String.class);
        int baseToConvertTo = context.getArgument(arg3Name, Integer.class);

        if (baseToConvertFrom == baseToConvertTo) {
            throw RADIXES_ARE_EQUIVALENT_NO_PREFIX.create(baseToConvertTo);
        }

        return getConversionLiteralComponentOrThrow(baseToConvertFrom, integerString, baseToConvertTo, "Conversion Result");
    }


}
