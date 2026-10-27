package base_n_calculator.shared.project_classes.commands.suggestion_providers;

import base_n_calculator.shared.project_constants.RadixPrefixes;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandSourceStack;

import java.util.concurrent.CompletableFuture;

public class RadixPrefixSuggestionProvider {
    public static class Shared implements SuggestionProvider<CommandSourceStack> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            for (String prefix : RadixPrefixes.getPrefixToRadixMap().keySet()) {
                builder.suggest(prefix);
            }

            return builder.buildFuture();
        }
    }

    public static class Client implements SuggestionProvider<FabricClientCommandSource> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            for (String prefix : RadixPrefixes.getPrefixToRadixMap().keySet()) {
                builder.suggest(prefix);
            }
            return builder.buildFuture();
        }
    }

}
