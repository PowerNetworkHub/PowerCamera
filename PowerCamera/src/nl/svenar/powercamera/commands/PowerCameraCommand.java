package nl.svenar.powercamera.commands;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class PowerCameraCommand {

    protected PowerCamera plugin;

    private final CommandExecutionContext ce;
    private final String command_name;

    protected PowerCameraCommand(PowerCamera plugin, String command_name, CommandExecutionContext ce) {
        this.plugin = plugin;
        this.ce = ce;
        this.command_name = command_name;
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);

    public CommandExecutionContext getCommandExecutor() {
        return this.ce;
    }

    public String getCommand_name() {
        return command_name;
    }
}
