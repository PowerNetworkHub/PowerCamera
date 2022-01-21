package nl.svenar.powercamera.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCommand;
import nl.svenar.powercamera.commands.PowerCommand.COMMAND_EXECUTOR;

public class PowerCommandHandler implements CommandExecutor, TabCompleter {

	private static HashMap<String, PowerCommand> powerCommands = new HashMap<String, PowerCommand>();

	private PowerCamera plugin;

	public PowerCommandHandler(PowerCamera plugin) {
		this.plugin = plugin;
	}

	private static boolean canExecuteCommand(CommandSender sender, PowerCommand command_handler) {
		return (sender instanceof Player && (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.PLAYER
				|| command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL))
				|| (sender instanceof ConsoleCommandSender
						&& (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.CONSOLE
								|| command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL))
				|| (sender instanceof BlockCommandSender
						&& (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.COMMANDBLOCK
								|| command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(plugin.getCommandHeader());
			sender.sendMessage(
					ChatColor.GREEN + "/" + commandLabel + " help" + ChatColor.DARK_GREEN + " - For the command list.");
			sender.sendMessage("");
			sender.sendMessage(
					ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + plugin.getDescription().getAuthors().get(0));
			sender.sendMessage(
					ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + plugin.getDescription().getVersion());
			// sender.sendMessage(
			// 		ChatColor.DARK_GREEN + "Website: " + ChatColor.GREEN + plugin.getDescription().getWebsite());
			sender.sendMessage(ChatColor.DARK_GREEN + "Support me: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
			sender.sendMessage(plugin.getCommandFooter());
		} else {
			String command = args[0];
			PowerCommand commandHandler = getPowerCommand(command);
			if (commandHandler != null) {
				boolean is_allowed = canExecuteCommand(sender, commandHandler);
				if (is_allowed) {

					int index = 1;
					if (args.length > 1) {

						while (index < args.length && commandHandler.getSubPowerCommand(args[index]) != null) {
							commandHandler = commandHandler.getSubPowerCommand(args[index]);
							index++;
						}
					}

					return commandHandler.onCommand(sender, cmd, commandLabel,
							Arrays.copyOfRange(args, index, args.length));
				} else {
					sender.sendMessage(
							plugin.pluginChatPrefix() + ChatColor.DARK_RED + "Only players can use this command");
				}
			} else {
				sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.DARK_RED + "Unknown Command");
			}

		}
		return false;
	}

	public static PowerCommand getPowerCommand(String command_name) {
		return powerCommands.get(command_name.toLowerCase());
	}

	public static void addPowerCommand(String command_name, PowerCommand command_handler) {
		powerCommands.put(command_name.toLowerCase(), command_handler);
	}

	public static ArrayList<String> handleTabComplete(CommandSender sender, String cmd, String[] args) {
		ArrayList<String> output = new ArrayList<String>();
		if (args.length == 0) {
			for (Entry<String, PowerCommand> entry : powerCommands.entrySet()) {
				if (cmd.length() == 0 || entry.getKey().toLowerCase().contains(cmd.toLowerCase())) {
					boolean is_allowed = canExecuteCommand(sender, entry.getValue());
					if (is_allowed) {
						output.add(entry.getKey());
					}
				}
			}
		} else {
			if (powerCommands.containsKey(cmd.toLowerCase())) {
				PowerCommand commandHandler = powerCommands.get(cmd.toLowerCase());

				int index = 0;
				if (args.length > 0) {

					while (index < args.length && commandHandler.getSubPowerCommand(args[index]) != null) {
						commandHandler = commandHandler.getSubPowerCommand(args[index]);
						index++;
					}
				}

				output = commandHandler.tabCompleteEvent(sender, Arrays.copyOfRange(args, index, args.length));
			}

		}
		return output;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> list = new ArrayList<String>();

		ArrayList<String> commandsList = new ArrayList<String>();

		for (String arg : PowerCommandHandler.handleTabComplete(sender, args[0],
				Arrays.copyOfRange(args, 1, args.length))) {
			commandsList.add(arg);
		}

		for (String cmd : commandsList) {
			if (cmd.toLowerCase().contains(args[args.length - 1].toLowerCase()))
				list.add(cmd);
		}

		return list;
	}

	public static HashMap<String, PowerCommand> getPowerCommands() {
		return powerCommands;
	}
}