package nl.svenar.powercamera.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.Util;

public class cmd_preview extends PowerCameraCommand {

	public cmd_preview(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powercamera.cmd.preview")) {
			if (this.plugin.player_camera_mode.get((Player) sender) == null || this.plugin.player_camera_mode.get((Player) sender) == PowerCamera.CAMERA_MODE.NONE) {
				if (args.length == 1) {
					String camera_name = plugin.player_selected_camera.get((Player) sender);
					if (camera_name != null) {
						int preview_time = plugin.getConfigPlugin().getConfig().getInt("point-preview-time");

						int num = Integer.parseInt(args[0]) - 1;

						List<String> camera_points = plugin.getConfigCameras().getPoints(camera_name);

						if (num < 0)
							num = 0;

						if (num > camera_points.size() - 1)
							num = camera_points.size() - 1;

						sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview started of point " + (num + 1) + "!");
						sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Ending in " + preview_time + " seconds.");

						GameMode previous_gamemode = ((Player) sender).getGameMode();
						Location previous_player_location = ((Player) sender).getLocation();
						Location point = Util.deserializeLocation(camera_points.get(num));
						boolean previous_invisible = ((Player) sender).isInvisible();

						plugin.player_camera_mode.put((Player) sender, PowerCamera.CAMERA_MODE.PREVIEW);
						if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode"))
							((Player) sender).setGameMode(GameMode.SPECTATOR);
						if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible"))
							((Player) sender).setInvisible(true);
						((Player) sender).teleport(point);

						new BukkitRunnable() {
							@Override
							public void run() {
								((Player) sender).teleport(previous_player_location);
								if (plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode"))
									((Player) sender).setGameMode(previous_gamemode);
								if (plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible"))
									((Player) sender).setInvisible(previous_invisible);
								plugin.player_camera_mode.put((Player) sender, PowerCamera.CAMERA_MODE.NONE);
								sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview ended!");
							}

						}.runTaskLater(this.plugin, preview_time * 20);

//				plugin.getConfigCameras().camera_removepoint(camera_name, num);
//				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Point removed from camera '" + camera_name + "'!");
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
