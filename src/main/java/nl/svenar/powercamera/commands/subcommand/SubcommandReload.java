package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@SuppressWarnings("PMD.CommentRequired")
public class SubcommandReload extends PowerCameraCommand {
    public SubcommandReload(final PowerCamera plugin, final String commandName) {
        super(plugin, commandName, CommandExecutionContext.ALL);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (sender.hasPermission("powercamera.cmd.reload")) {
            plugin.getConfigCameras().reloadConfig();
            sendMessage(sender, ChatColor.GREEN + "Config and cameras reloaded!");
        } else {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
        }
        return true;
    }
}
