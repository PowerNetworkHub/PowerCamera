package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.Util;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.PlayerCameraData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandSetDuration extends PowerCameraCommand {

    public SubcommandSetDuration(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerCameraData cameraData = plugin.getPlayerData().get(player);


        if (sender.hasPermission("powercamera.cmd.setduration")) {
            if (args.length == 1) {
                int duration = Util.timeStringToSecondsConverter(args[0]);

                if (duration > 0) {
                    String cameraName = cameraData.getSelectedCameraId();
                    if (cameraName != null) {
                        plugin.getConfigCameras().setDuration(cameraName, duration);
                        sender.sendMessage(
                            plugin.getPluginChatPrefix() + ChatColor.GREEN + "Camera path duration set to: " + duration + " seconds on camera '" + cameraName
                                + "'");
                    } else {
                        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "No camera selected!");
                        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
                    }
                } else {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Duration must be greater than 0");
                }

            } else {
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " setduration <duration>");
            }

        } else {
            sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
        }

        return false;
    }
}
