package nl.svenar.powercamera.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.PowerCamera;

public class cmd_addcommand extends PowerCameraCommand {

	public cmd_addcommand(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powercamera.cmd.addpoint")) {
			if (args.length > 0) {
				String camera_name = plugin.player_selected_camera.get(((Player) sender).getUniqueId());
				if (camera_name != null) {
					String command = String.join(" ", args);
					plugin.getConfigCameras().camera_addcommand(command, camera_name);
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Command added to camera '" + camera_name + "'!");
				} else {
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "No camera selected!");
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
				}

			} else {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " addcommand <command>");
			}

		} else {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
		}

		return false;
	}
}
