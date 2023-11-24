package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandAddPoint extends PowerCameraCommand {

    public SubcommandAddPoint(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender.hasPermission("powercamera.cmd.addpoint")) {
            String easing = "linear";
            if (args.length == 0) {
                String cameraName = plugin.playerSelectedCamera.get(((Player) sender).getUniqueId());
                if (cameraName != null) {
                    plugin.getConfigCameras().camera_addpoint(((Player) sender).getLocation(), easing, cameraName);
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Point added to camera '" + cameraName + "'!");
                } else {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "No camera selected!");
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
                }

            } else if (args.length == 1) {
                String cameraName = plugin.playerSelectedCamera.get(((Player) sender).getUniqueId());
                easing = args[0];
                if (easing.equalsIgnoreCase("linear") || easing.equalsIgnoreCase("teleport")) {
                    if (cameraName != null) {
                        plugin.getConfigCameras().camera_addpoint(((Player) sender).getLocation(), easing, cameraName);
                        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Point added to camera '" + cameraName + "'!");
                    } else {
                        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "No camera selected!");
                        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
                    }
                } else {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " addpoint [linear/teleport]");
                }

            } else {
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " addpoint [linear/teleport]");
            }

        } else {
            sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
        }

        return false;
    }
}
