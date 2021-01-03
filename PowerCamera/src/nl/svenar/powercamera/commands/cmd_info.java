package nl.svenar.powercamera.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.Util;

public class cmd_info extends PowerCameraCommand {

	public cmd_info(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powercamera.cmd.info")) {
			if (args.length == 0) {
				String camera_name = plugin.player_selected_camera.get(((Player) sender).getUniqueId());
				if (camera_name != null) {
					List<String> camera_points = plugin.getConfigCameras().getPoints(camera_name);
					int camera_duration = plugin.getConfigCameras().getDuration(camera_name);

					sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getPluginDescriptionFile().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
					sender.sendMessage(ChatColor.DARK_GREEN + "Camera name: " + ChatColor.GREEN + camera_name);
					sender.sendMessage(ChatColor.DARK_GREEN + "Path duration: " + ChatColor.GREEN + camera_duration + " seconds");
					sender.sendMessage(ChatColor.DARK_GREEN + "Camera points (" + ChatColor.GREEN + camera_points.size() + ChatColor.DARK_GREEN + "):");
					for (String point : camera_points) {
						String point_info = "";
						Location point_location = Util.deserializeLocation(point);

						point_info += point_location.getWorld().getName();
						point_info += ", (X: " + point_location.getBlockX() + ", Y: " + point_location.getBlockY() + ", Z: " + point_location.getBlockZ() + ")";
						point_info += ", (Yaw: " + Math.round(point_location.getYaw()) + ", Pitch: " + Math.round(point_location.getPitch()) + ")";

						sender.sendMessage(ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + point_info);
					}
					sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");
				} else {
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "No camera selected!");
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
				}

			} else {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " info");
			}

		} else {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
		}

		return false;
	}
}
