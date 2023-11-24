package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SubcommandRemove extends PowerCameraCommand {

    public SubcommandRemove(PowerCamera plugin, String command_name) {
        super(plugin, command_name, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender.hasPermission("powercamera.cmd.remove")) {
            if (args.length == 1) {
                String camera_name = args[0];
                if (plugin.getConfigCameras().remove_camera(camera_name)) {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Camera '" + camera_name + "' deleted!");
                } else {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "A camera with the name '" + camera_name + "' does not exists!");
                }

            } else {
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " remove <name>");
            }

        } else {
            sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
        }

        return false;
    }
}
