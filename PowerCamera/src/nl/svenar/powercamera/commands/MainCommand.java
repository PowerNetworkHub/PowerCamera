package nl.svenar.powercamera.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.PowerCamera;

public class MainCommand implements CommandExecutor {

	private static HashMap<String, PowerCameraCommand> powercamera_commands = new HashMap<String, PowerCameraCommand>();

	private PowerCamera plugin;

	public MainCommand(PowerCamera plugin) {
		this.plugin = plugin;

		new cmd_help(plugin, "help");
		new cmd_create(plugin, "create");
		new cmd_remove(plugin, "remove");
		new cmd_addpoint(plugin, "addpoint");
		new cmd_delpoint(plugin, "delpoint");
		new cmd_select(plugin, "select");
		new cmd_preview(plugin, "preview");
		new cmd_info(plugin, "info");
		new cmd_setduration(plugin, "setduration");
		new cmd_start(plugin, "start");
		new cmd_stats(plugin, "stats");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + plugin.getPluginDescriptionFile().getName() + ChatColor.DARK_AQUA + "--------");
			sender.sendMessage(ChatColor.GREEN + "/" + commandLabel + " help" + ChatColor.DARK_GREEN + " - For the command list.");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + plugin.getPluginDescriptionFile().getAuthors().get(0));
			sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + plugin.getPluginDescriptionFile().getVersion());
			sender.sendMessage(ChatColor.DARK_GREEN + "Website: " + ChatColor.GREEN + plugin.website_url);
			sender.sendMessage(ChatColor.DARK_GREEN + "Support me: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
			sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
		} else {
			String command = args[0];
			PowerCameraCommand command_handler = get_powercamera_command(command);
			if (command_handler != null) {
				if (sender instanceof Player) {
					return command_handler.onCommand(sender, cmd, commandLabel, Arrays.copyOfRange(args, 1, args.length));
				} else {
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Only players can use this command");
				}
			} else {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Unknown Command");
			}

		}
		return false;
	}

	public static PowerCameraCommand get_powercamera_command(String command_name) {
		return powercamera_commands.get(command_name.toLowerCase());
	}

	public static void add_powercamera_command(String command_name, PowerCameraCommand command_handler) {
		powercamera_commands.put(command_name.toLowerCase(), command_handler);
	}
}
