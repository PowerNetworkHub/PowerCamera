package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@SuppressWarnings({"PMD.CommentRequired", "PMD.MethodArgumentCouldBeFinal", "PMD.ShortVariable", "PMD.UseVarargs"})
public abstract class PowerCameraCommand {

    protected PowerCamera plugin;

    private final CommandExecutionContext ce;
    private final String commandName;

    protected PowerCameraCommand(PowerCamera plugin, String commandName, CommandExecutionContext ce) {
        this.plugin = plugin;
        this.ce = ce;
        this.commandName = commandName;
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);

    public CommandExecutionContext getCommandExecutor() {
        return this.ce;
    }

    public String getCommandName() {
        return commandName;
    }

    protected void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(plugin.getPluginChatPrefix() + message);
    }
}
