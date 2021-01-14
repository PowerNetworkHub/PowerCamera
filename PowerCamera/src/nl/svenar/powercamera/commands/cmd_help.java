package nl.svenar.powercamera.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.PowerCamera;

public class cmd_help extends PowerCameraCommand {

	public cmd_help(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.ALL);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			if (sender.hasPermission("powercamera.cmd.help")) {
				String tellrawbase = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\"},{\"text\":\"/%cmd% %arg%\",\"color\":\"green\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/%cmd% %arg%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"/%cmd% %arg%\"}},{\"text\":\"]\",\"color\":\"black\"},{\"text\":\" %help%\",\"color\":\"dark_green\"}]";
				ArrayList<String> help_messages = new ArrayList<String>();

				help_messages.add(
						"tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"%plugin%\",\"color\":\"aqua\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]"
								.replaceAll("%plugin%", plugin.getPluginDescriptionFile().getName()).replaceAll("%player%", sender.getName()));

				help_messages.add(tellrawbase.replaceAll("%arg%", "create <name>").replaceAll("%help%", "Create a new camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "remove <name>").replaceAll("%help%", "Remove a camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "addpoint").replaceAll("%help%", "Add an point to a camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "addcommand <command>").replaceAll("%player%", sender.getName()).replaceAll("%help%", "Add an command to a camera path, %player% gets replaced with the player's name").replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "delpoint [point_number]").replaceAll("%help%", "Remove an point from acamera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "select <name>").replaceAll("%help%", "Select a camera path by name").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "preview <point_number>").replaceAll("%help%", "Preview a point on the selected camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "info").replaceAll("%help%", "Info about the currently selected camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "setduration <duration>").replaceAll("%help%", "Set the total duration of the current camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "start [cameraname]").replaceAll("%help%", "Run the camera").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "startother <playername> <cameraname>").replaceAll("%help%", "Run a camera for a different player").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "stop").replaceAll("%help%", "Stop the camera").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
				help_messages.add(tellrawbase.replaceAll("%arg%", "stats").replaceAll("%help%", "Show plugin stats").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));

				help_messages.add(
						"tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"-------------------------------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]".replaceAll("%player%", sender.getName()));

				if (plugin != null)
					for (String msg : help_messages)
						plugin.getServer().dispatchCommand((CommandSender) plugin.getServer().getConsoleSender(), msg);

			} else {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
			}
		} else {
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getPluginDescriptionFile().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");

			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " create <name>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Create a new camera path");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " remove <name>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Remove a camera path");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " addpoint" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Add an point to a camera path");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " addcommand <command>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Add an command to a camera path, %player% gets replaced with the player's name");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " delpoint [point_number]" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Remove an point from a camera path");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " select <name>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Select a camera path by name");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " preview <point_number>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Preview a point on the selected camera path");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " info" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Info about the currently selected camera path");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " setduration <duration>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Set the total duration of the current camera path");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " start [cameraname]" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Run the camera");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " stop" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Stop the camera");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.GREEN + "/" + commandLabel + " startother <playername> <cameraname>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Run a camera for a different player");
			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.GREEN + "/" + commandLabel + " stats" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Show plugin stats");

			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");
		}

		return false;
	}
}
