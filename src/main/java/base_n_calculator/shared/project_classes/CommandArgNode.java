package base_n_calculator.shared.project_classes;

import base_n_calculator.shared.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CommandArgNode<TParseValue> extends Node<CommandArgNode<?>> {
    private final Function<String, Boolean> verificationCallback;
    private final Function<String, TParseValue> parseCallback;
    private final List<String> autocompleteSuggestions;

    public CommandArgNode(String name, Function<String, Boolean> verificationCallback) {
        this(name, verificationCallback, null, null, null);
    }
    public CommandArgNode(String name, Function<String, Boolean> verificationCallback, Function<String, TParseValue> parseCallback) {
        this(name, verificationCallback, parseCallback, null, null);
    }

    public CommandArgNode(String name, Function<String, Boolean> verificationCallback, Function<String, TParseValue> parseCallback, List<String> autocompleteSuggestions) {
        this(name, verificationCallback, parseCallback, autocompleteSuggestions, null);
    }
    public CommandArgNode(String name, Function<String, Boolean> verificationCallback, Function<String, TParseValue> parseCallback, List<String> autocompleteSuggestions, CommandArgNode<?> parent) {
        super(name, parent);
        this.verificationCallback = verificationCallback;
        this.parseCallback = parseCallback;
        this.autocompleteSuggestions = List.copyOf(autocompleteSuggestions);
    }

    public TParseValue parse(String literal) {
        return this.parseCallback.apply(literal);
    };

    public boolean verify(String literal) {
        return this.verificationCallback.apply(literal);
    }

    public String formatName() {
        return "<" + StringUtils.toSnakeCase(this.getName()) + ">";
    }

    public List<String> autocomplete() {
        return List.copyOf(this.autocompleteSuggestions);
    }
}

