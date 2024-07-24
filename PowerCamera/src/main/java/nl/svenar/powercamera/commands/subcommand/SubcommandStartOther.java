package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.CameraMode;
import nl.svenar.powercamera.data.PlayerCameraData;
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
        if (args.length != 2) {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " startother <playername> <cameraname>");
            return false;
        }

        String targetName = args[0];
        String cameraName = args[1];

        if (!sender.hasPermission("powercamera.cmd.startother")) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(targetName);

        if (targetPlayer == null) {
            sendMessage(sender, ChatColor.DARK_RED + "Player '" + targetName + "' not found or is offline!");
            return false;
        }

        PlayerCameraData cameraData = plugin.getPlayerData().get(targetPlayer);

        if (cameraData.getCameraMode() != CameraMode.NONE) {
            sendMessage(sender, ChatColor.DARK_RED + "Player '" + targetPlayer.getName() + "' already has a camera active!");
            return false;
        }

        if (this.plugin.getConfigCameras().cameraExists(cameraName)) {
            cameraData.setCameraHandler(new CameraHandler(plugin, targetPlayer, cameraName).generatePath().start());
            sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Playing '" + cameraName + "' on player: " + targetPlayer.getName());
        } else {
            sendMessage(sender, ChatColor.RED + "Camera '" + cameraName + "' not found!");
        }

        return false;
    }
}
