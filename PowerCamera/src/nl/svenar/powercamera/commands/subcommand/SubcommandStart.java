package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.CameraMode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandStart extends PowerCameraCommand {

    public SubcommandStart(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("powercamera.cmd.start")) {
                if (this.plugin.playerCameraMode.get(((Player) sender).getUniqueId()) == null
                    || this.plugin.playerCameraMode.get(((Player) sender).getUniqueId()) == CameraMode.NONE) {
                    String cameraName = plugin.playerSelectedCamera.get(((Player) sender).getUniqueId());
                    if (cameraName != null) {
                        this.plugin.playerCameraHandler.put(((Player) sender).getUniqueId(),
                            new CameraHandler(plugin, (Player) sender, cameraName).generatePath().start());
                    } else {
                        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "No camera selected!");
                        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
                    }
                } else {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Camera already active!");
                }
            } else {
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " start");
            }

        } else if (args.length == 1) {
            String cameraName = args[0];

            if (sender.hasPermission("powercamera.cmd.start." + cameraName.toLowerCase())) {
                if (this.plugin.playerCameraMode.get(((Player) sender).getUniqueId()) == null
                    || this.plugin.playerCameraMode.get(((Player) sender).getUniqueId()) == CameraMode.NONE) {
                    if (this.plugin.getConfigCameras().cameraExists(cameraName)) {
                        this.plugin.playerCameraHandler.put(((Player) sender).getUniqueId(),
                            new CameraHandler(plugin, (Player) sender, cameraName).generatePath().start());
                    } else {
                        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "Camera '" + cameraName + "' not found!");
                    }
                } else {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Camera already active!");
                }
            } else {
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
            }
        } else {
            sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " start");
        }

        return false;
    }
}
