package nl.svenar.powercamera.commands.structure;

import java.util.function.Predicate;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"PMD.CommentRequired", "PMD.MethodArgumentCouldBeFinal"})
public enum CommandExecutionContext {
    NONE((sender) -> false),
    PLAYER(Player.class::isInstance),
    CONSOLE(ConsoleCommandSender.class::isInstance),
    ALL((sender) -> true);

    private final Predicate<CommandSender> predicate;

    CommandExecutionContext(Predicate<CommandSender> predicate) {
        this.predicate = predicate;
    }

    public boolean isAllowed(CommandSender sender) {
        return predicate.test(sender);
    }
}
