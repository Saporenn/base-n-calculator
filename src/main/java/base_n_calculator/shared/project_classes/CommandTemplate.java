package base_n_calculator.shared.project_classes;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class CommandTemplate {
    public final String name;
    public final Integer id;
    public final List<String> aliases;
    public final List<CommandArgNode<?>> argumentPaths;

    public CommandTemplate(String name, Integer id) {
        this(name, id, null, null);
    }
    public CommandTemplate(String name, Integer id, List<String> aliases) {
        this(name, id, aliases, null);
    }
    public CommandTemplate(String name, Integer id, List<String> aliases, CommandArgNode<?>[] argumentPaths) {
        this.name = name;
        this.id = id;
        this.aliases = (aliases != null)
                ? aliases
                : new ArrayList<>();

        this.argumentPaths = (argumentPaths != null)
                ? Arrays.asList(argumentPaths)
                : new ArrayList<>();
    }

    public CommandTemplate addAliases(String... aliases) {
        this.aliases.addAll(List.of(aliases));
        return this;
    }

    public CommandTemplate addArgumentPath(CommandArgNode<?> argumentPath) {
        this.argumentPaths.add(argumentPath);
        return this;
    }

    public CommandTemplate addSubcommandPath() {
        return this;
    }


}
