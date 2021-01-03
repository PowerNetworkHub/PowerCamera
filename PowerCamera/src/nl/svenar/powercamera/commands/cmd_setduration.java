package nl.svenar.powercamera.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.Util;

public class cmd_setduration extends PowerCameraCommand {

	public cmd_setduration(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powercamera.cmd.setduration")) {
			if (args.length == 1) {
				int duration = Util.timeStringToSecondsConverter(args[0]);

				if (duration > 0) {
					String camera_name = plugin.player_selected_camera.get(((Player) sender).getUniqueId());
					if (camera_name != null) {
						plugin.getConfigCameras().setDuration(camera_name, duration);
						sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Camera path duration set to: " + duration + " seconds on camera '" + camera_name + "'");
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
