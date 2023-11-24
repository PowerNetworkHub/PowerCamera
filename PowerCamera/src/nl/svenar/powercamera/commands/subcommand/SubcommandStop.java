package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandStop extends PowerCameraCommand {

    public SubcommandStop(PowerCamera plugin, String command_name) {
        super(plugin, command_name, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("powercamera.cmd.stop")) {
                if (this.plugin.player_camera_mode.get(((Player) sender).getUniqueId()) != null
                    && this.plugin.player_camera_mode.get(((Player) sender).getUniqueId()) != PowerCamera.CAMERA_MODE.NONE
                    && this.plugin.player_camera_handler.get(((Player) sender).getUniqueId()) != null) {
                    this.plugin.player_camera_handler.get(((Player) sender).getUniqueId()).stop();
                    if (!sender.hasPermission("powercamera.hidestartmessages")) {
                        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Current camera stopped");
                    }
                } else {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "No camera active!");
                }
            } else {
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
            }
        }

        return false;
    }
}
