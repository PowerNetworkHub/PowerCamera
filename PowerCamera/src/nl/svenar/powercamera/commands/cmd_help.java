package nl.svenar.powercamera.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;

public class cmd_help extends PowerCameraCommand {

	public cmd_help(PowerCamera plugin, String command_name) {
		super(plugin, command_name);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powercamera.cmd.help")) {
			String tellrawbase = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\"},{\"text\":\"/%cmd% %arg%\",\"color\":\"green\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/%cmd% %arg%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"/%cmd% %arg%\"}},{\"text\":\"]\",\"color\":\"black\"},{\"text\":\" %help%\",\"color\":\"dark_green\"}]";
			ArrayList<String> help_messages = new ArrayList<String>();

			help_messages.add(
					"tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"%plugin%\",\"color\":\"aqua\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]"
							.replaceAll("%plugin%", plugin.getPluginDescriptionFile().getName()).replaceAll("%player%", sender.getName()));

			help_messages.add(tellrawbase.replaceAll("%arg%", "create <name>").replaceAll("%help%", "Create a new camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
			help_messages.add(tellrawbase.replaceAll("%arg%", "remove <name>").replaceAll("%help%", "Remove a camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
			help_messages.add(tellrawbase.replaceAll("%arg%", "addpoint").replaceAll("%help%", "Add an point to a camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
			help_messages.add(tellrawbase.replaceAll("%arg%", "delpoint [point-number]").replaceAll("%help%", "Remove an point from acamera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
			help_messages.add(tellrawbase.replaceAll("%arg%", "select <name>").replaceAll("%help%", "Select a camera path my name").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
			help_messages.add(tellrawbase.replaceAll("%arg%", "preview <point-number>").replaceAll("%help%", "Preview a point on the selected camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
			help_messages.add(tellrawbase.replaceAll("%arg%", "info").replaceAll("%help%", "Info about the currently selected camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
			help_messages.add(tellrawbase.replaceAll("%arg%", "setduration <duration>").replaceAll("%help%", "Set the total duration of the current camera path").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
			help_messages.add(tellrawbase.replaceAll("%arg%", "start").replaceAll("%help%", "Run the camera").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));
			help_messages.add(tellrawbase.replaceAll("%arg%", "stats").replaceAll("%help%", "Show plugin stats").replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel));

			help_messages
					.add("tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"-------------------------------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]".replaceAll("%player%", sender.getName()));

			if (plugin != null)
				for (String msg : help_messages)
					plugin.getServer().dispatchCommand((CommandSender) plugin.getServer().getConsoleSender(), msg);

		} else {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
		}

		return false;
	}
}
