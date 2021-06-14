package nl.svenar.powercamera.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand.COMMAND_EXECUTOR;

public class MainCommand implements CommandExecutor {

	private static HashMap<String, PowerCameraCommand> powercamera_commands = new HashMap<String, PowerCameraCommand>();

	private PowerCamera plugin;

	public MainCommand(PowerCamera plugin) {
		this.plugin = plugin;

		new cmd_help(plugin, "help");
		new cmd_create(plugin, "create");
		new cmd_remove(plugin, "remove");
		new cmd_addpoint(plugin, "addpoint");
		new cmd_addcommand(plugin, "addcommand");
		new cmd_delpoint(plugin, "delpoint");
		new cmd_select(plugin, "select");
		new cmd_preview(plugin, "preview");
		new cmd_info(plugin, "info");
		new cmd_setduration(plugin, "setduration");
		new cmd_start(plugin, "start");
		new cmd_startother(plugin, "startother");
		new cmd_stop(plugin, "stop");
		new cmd_stats(plugin, "stats");
		new cmd_invisible(plugin, "invisible");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getPluginDescriptionFile().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
			sender.sendMessage(ChatColor.GREEN + "/" + commandLabel + " help" + ChatColor.DARK_GREEN + " - For the command list.");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + plugin.getPluginDescriptionFile().getAuthors().get(0));
			sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + plugin.getPluginDescriptionFile().getVersion());
			sender.sendMessage(ChatColor.DARK_GREEN + "Website: " + ChatColor.GREEN + plugin.website_url);
			sender.sendMessage(ChatColor.DARK_GREEN + "Support me: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");
		} else {
			String command = args[0];
			PowerCameraCommand command_handler = get_powercamera_command(command);
			if (command_handler != null) {
				boolean is_allowed = (sender instanceof Player && (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.PLAYER || command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL))
						|| sender instanceof ConsoleCommandSender && (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.CONSOLE || command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL);
				if (is_allowed) {
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
