package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.CameraMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandStartOther extends PowerCameraCommand {

    public SubcommandStartOther(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.ALL);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 2) {
            String targetName = args[0];
            String cameraName = args[1];
            if (sender.hasPermission("powercamera.cmd.startother")) {
                Player targetPlayer = Bukkit.getPlayer(targetName);

                if (targetPlayer != null) {
                    if (this.plugin.playerCameraMode.get(targetPlayer.getUniqueId()) == null
                        || this.plugin.playerCameraMode.get(targetPlayer.getUniqueId()) == CameraMode.NONE) {
                        if (this.plugin.getConfigCameras().cameraExists(cameraName)) {
                            this.plugin.playerCameraHandler.put(targetPlayer.getUniqueId(),
                                new CameraHandler(plugin, targetPlayer, cameraName).generatePath().start());
                            sender.sendMessage(
                                plugin.getPluginChatPrefix() + ChatColor.GREEN + "Playing '" + cameraName + "' on player: " + targetPlayer.getName());
                        } else {
                            sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "Camera '" + cameraName + "' not found!");
                        }
                    } else {
                        sender.sendMessage(
                            plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Player '" + targetPlayer.getName() + "' already has a camera active!");
                    }
                } else {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Player '" + targetName + "' not found or is offline!");
                }
            } else {
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
            }
        } else {
            sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " startother <playername> <cameraname>");
        }

        return false;
    }
}
