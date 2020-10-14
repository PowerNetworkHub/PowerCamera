package nl.svenar.powercamera.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;

public class cmd_start extends PowerCameraCommand {

	public cmd_start(PowerCamera plugin, String command_name) {
		super(plugin, command_name);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			if (sender.hasPermission("powercamera.cmd.start")) {
				if (this.plugin.player_camera_active.get((Player) sender) == null || !this.plugin.player_camera_active.get((Player) sender)) {
					String camera_name = plugin.player_selected_camera.get((Player) sender);
					if (camera_name != null) {
						new CameraHandler(plugin, (Player) sender, camera_name).generatePath().start();
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
			String camera_name = args[0];

			if (sender.hasPermission("powercamera.cmd.start." + camera_name.toLowerCase())) {
				if (this.plugin.player_camera_active.get((Player) sender) == null || !this.plugin.player_camera_active.get((Player) sender)) {
					if (this.plugin.getConfigCameras().camera_exists(camera_name)) {
						new CameraHandler(plugin, (Player) sender, camera_name).generatePath().start();
					} else {
						sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "Camera '" + camera_name + "' not found!");
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
