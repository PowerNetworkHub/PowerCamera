package nl.svenar.powercamera.commands;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;

public class cmd_preview extends PowerCameraCommand {

	public cmd_preview(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powercamera.cmd.preview")) {
			if (this.plugin.player_camera_mode.get(((Player) sender).getUniqueId()) == null || this.plugin.player_camera_mode.get(((Player) sender).getUniqueId()) == PowerCamera.CAMERA_MODE.NONE) {
				if (args.length == 1) {
					String camera_name = plugin.player_selected_camera.get(((Player) sender).getUniqueId());
					if (camera_name != null) {
						int preview_time = plugin.getConfigPlugin().getConfig().getInt("point-preview-time");

						int num = Integer.parseInt(args[0]) - 1;

						this.plugin.player_camera_handler.put(((Player) sender).getUniqueId(), new CameraHandler(plugin, (Player) sender, camera_name).generatePath().preview((Player) sender, num, preview_time));

					} else {
						sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "No camera selected!");
						sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
					}

				} else {
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " preview <point-number>");
				}
			} else {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Camera already active!");
			}
		} else {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
		}

		return false;
	}
}
