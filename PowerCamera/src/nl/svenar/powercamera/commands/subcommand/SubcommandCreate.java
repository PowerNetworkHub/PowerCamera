package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandCreate extends PowerCameraCommand {

    public SubcommandCreate(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender.hasPermission("powercamera.cmd.create")) {
            if (args.length == 1) {
                String cameraName = args[0];
                if (plugin.getConfigCameras().createCamera(cameraName)) {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Camera '" + cameraName + "' created!");
//					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Select this camera by doing: /" + commandLabel + " select " + cameraName + "");
                    plugin.playerSelectedCamera.put(((Player) sender).getUniqueId(), plugin.getConfigCameras().getCameraNameIgnorecase(cameraName));
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Camera '" + cameraName + "' selected!");
                } else {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "A camera with the name '" + cameraName + "' already exists!");
                }

            } else {
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " create <name>");
            }

        } else {
            sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
        }

        return false;
    }
}
