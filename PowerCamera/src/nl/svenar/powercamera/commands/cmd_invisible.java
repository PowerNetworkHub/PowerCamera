package nl.svenar.powercamera.commands;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.PowerCamera;

public class cmd_invisible extends PowerCameraCommand {

	public cmd_invisible(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if (sender.hasPermission("powercamera.cmd.invisible")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false")) {
					boolean set_invisible = args[0].equalsIgnoreCase("true");
					player.setInvisible(set_invisible);
				} else {
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel
							+ " invisible <true/false>");
				}
			} else {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel
						+ " invisible <true/false>");
			}
		} else {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED
					+ "You do not have permission to execute this command");
		}

		return false;
	}
}
