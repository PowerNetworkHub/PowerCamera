package nl.svenar.powercamera.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.handlers.PowerCommandHandler;
import nl.svenar.powercamera.utils.PaginationManager;

public class CommandHelp extends PowerCommand {

    public CommandHelp(PowerCamera plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "[page]";
    }

    @Override
    public String getDescription() {
        return "PowerCamera command list";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powercamera.command.help")) {
            sender.sendMessage(
                    plugin.pluginChatPrefix() + ChatColor.RED + "You do not have permission to execute this command.");
            return false;
        }

        HashMap<String, String> helpData = new HashMap<String, String>();

        for (Entry<String, PowerCommand> powerCommand : PowerCommandHandler.getPowerCommands().entrySet()) {
            if (powerCommand.getValue().showInHelp()) {
                helpData.put(
                        powerCommand.getKey()
                                + (powerCommand.getValue().getArgumentSuggestions().length() > 0 ? " " : "")
                                + powerCommand.getValue().getArgumentSuggestions(),
                        powerCommand.getValue().getDescription());
            }

            HashMap<String, PowerCommand> subCommands = powerCommand.getValue().getNestedSubPowerCommands();
            for (Entry<String, PowerCommand> subPowerCommand : subCommands.entrySet()) {
                if (powerCommand.getValue().showInHelp() && subPowerCommand.getValue().showInHelp()) {
                    helpData.put(
                            powerCommand.getKey() + " " + subPowerCommand.getKey() + " "
                                    + subPowerCommand.getValue().getArgumentSuggestions(),
                            subPowerCommand.getValue().getDescription());
                }
            }
        }

        TreeMap<String, String> sortedHelpData = new TreeMap<String, String>();
        sortedHelpData.putAll(helpData);

        ArrayList<String> helpLines = new ArrayList<String>();
        for (Entry<String, String> entry : sortedHelpData.entrySet()) {
            helpLines.add(ChatColor.BLACK + "[" + ChatColor.DARK_GREEN
                    + (sender instanceof ConsoleCommandSender ? "" : "/") + commandLabel + " " + entry.getKey()
                    + ChatColor.BLACK + "] " + ChatColor.GREEN + entry.getValue());
        }

        int page = Integer.MIN_VALUE;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]) - 1;

            } catch (NumberFormatException nfe) {
                sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + "Invalid number given as argument.");
            }
        } else {
            page = 0;
        }

        if (page > Integer.MIN_VALUE) {
            PaginationManager paginationManager = new PaginationManager(helpLines, "help", commandLabel + " help", page,
                    5);
            paginationManager.send(sender);
        }
        return false;
    }

    @Override
    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        return new ArrayList<String>();
    }
}