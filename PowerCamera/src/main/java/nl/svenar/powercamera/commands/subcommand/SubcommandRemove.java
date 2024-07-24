package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SubcommandRemove extends PowerCameraCommand {

    public SubcommandRemove(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powercamera.cmd.remove")) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        if (args.length != 1) {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " remove <name>");
        }

        String cameraName = args[0];
        if (plugin.getConfigCameras().removeCamera(cameraName)) {
            sendMessage(sender, ChatColor.GREEN + "Camera '" + cameraName + "' deleted!");
        } else {
            sendMessage(sender, ChatColor.RED + "A camera with the name '" + cameraName + "' does not exists!");
        }

        return false;
    }
}
