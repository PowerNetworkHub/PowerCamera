package nl.svenar.powercamera.commands;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;

public class cmd_stats extends PowerCameraCommand {

	public cmd_stats(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.ALL);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powercamera.cmd.stats")) {
			if (args.length == 0) {
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				format.setTimeZone(TimeZone.getTimeZone("UTC"));
				Instant current_time = Instant.now();

				String invisibility_mode = "None";

				if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode") && this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible")) {
					invisibility_mode = "spectator & invisible";
				} else {
					if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode")) {
						invisibility_mode = "specator";
					}

					if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible")) {
						invisibility_mode = "invisible";
					}
				}

				sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getPluginDescriptionFile().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
				sender.sendMessage(ChatColor.DARK_GREEN + "Server version: " + ChatColor.GREEN + Bukkit.getVersion());
				sender.sendMessage(ChatColor.DARK_GREEN + "Bukkit version: " + ChatColor.GREEN + Bukkit.getServer().getBukkitVersion());
				sender.sendMessage(ChatColor.DARK_GREEN + "Java version: " + ChatColor.GREEN + System.getProperty("java.version"));
				sender.sendMessage(ChatColor.DARK_GREEN + plugin.getPluginDescriptionFile().getName() + " Version: " + ChatColor.GREEN + plugin.getPluginDescriptionFile().getVersion());
				sender.sendMessage(ChatColor.DARK_GREEN + "Plugin uptime: " + ChatColor.GREEN + format.format(Duration.between(plugin.powercamera_start_time, current_time).toMillis()));
				sender.sendMessage(ChatColor.DARK_GREEN + "Registered cameras: " + ChatColor.GREEN + plugin.getConfigCameras().getCameras().size());
				sender.sendMessage(ChatColor.DARK_GREEN + "Registered players: " + ChatColor.GREEN + plugin.getConfigCameras().getPlayers().size());
				sender.sendMessage(ChatColor.DARK_GREEN + "Invisibility mode: " + ChatColor.GREEN + invisibility_mode);
				sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");

			} else {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " stats");
			}
		} else {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
		}

		return false;
	}
}
