package nl.svenar.powercamera.commands.subcommand;

import java.util.ArrayList;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandHelp extends PowerCameraCommand {

    public SubcommandHelp(PowerCamera plugin, String command_name) {
        super(plugin, command_name, CommandExecutionContext.ALL);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("powercamera.cmd.help")) {
                String tellrawbase = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\"},{\"text\":\"/%cmd% %arg%\",\"color\":\"green\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/%cmd% %arg%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"/%cmd% %arg%\"}},{\"text\":\"]\",\"color\":\"black\"},{\"text\":\" %help%\",\"color\":\"dark_green\"}]";
                ArrayList<String> help_messages = new ArrayList<String>();

                help_messages.add(
                    "tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"%plugin%\",\"color\":\"aqua\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]"
                        .replace("%plugin%", plugin.getPluginDescriptionFile().getName()).replace("%player%", sender.getName()));

                help_messages.add(
                    tellrawbase.replace("%arg%", "create <name>").replace("%help%", "Create a new camera path").replace("%player%", sender.getName())
                        .replace("%cmd%", commandLabel));
                help_messages.add(
                    tellrawbase.replace("%arg%", "remove <name>").replace("%help%", "Remove a camera path").replace("%player%", sender.getName())
                        .replace("%cmd%", commandLabel));
                help_messages.add(
                    tellrawbase.replace("%arg%", "addpoint").replace("%help%", "Add an point to a camera path").replace("%player%", sender.getName())
                        .replace("%cmd%", commandLabel));
                help_messages.add(tellrawbase.replace("%arg%", "addcommand <command>").replace("%player%", sender.getName())
                    .replace("%help%", "Add an command to a camera path, %player% gets replaced with the player's name").replace("%cmd%", commandLabel));
                help_messages.add(tellrawbase.replace("%arg%", "delpoint [point_number]").replace("%help%", "Remove an point from acamera path")
                    .replace("%player%", sender.getName()).replace("%cmd%", commandLabel));
                help_messages.add(tellrawbase.replace("%arg%", "select <name>").replace("%help%", "Select a camera path by name")
                    .replace("%player%", sender.getName()).replace("%cmd%", commandLabel));
                help_messages.add(tellrawbase.replace("%arg%", "preview <point_number>").replace("%help%", "Preview a point on the selected camera path")
                    .replace("%player%", sender.getName()).replace("%cmd%", commandLabel));
                help_messages.add(tellrawbase.replace("%arg%", "info").replace("%help%", "Info about the currently selected camera path")
                    .replace("%player%", sender.getName()).replace("%cmd%", commandLabel));
                help_messages.add(
                    tellrawbase.replace("%arg%", "setduration <duration>").replace("%help%", "Set the total duration of the current camera path")
                        .replace("%player%", sender.getName()).replace("%cmd%", commandLabel));
                help_messages.add(
                    tellrawbase.replace("%arg%", "start [cameraname]").replace("%help%", "Run the camera").replace("%player%", sender.getName())
                        .replace("%cmd%", commandLabel));
                help_messages.add(
                    tellrawbase.replace("%arg%", "startother <playername> <cameraname>").replace("%help%", "Run a camera for a different player")
                        .replace("%player%", sender.getName()).replace("%cmd%", commandLabel));
                help_messages.add(tellrawbase.replace("%arg%", "stop").replace("%help%", "Stop the camera").replace("%player%", sender.getName())
                    .replace("%cmd%", commandLabel));
                help_messages.add(tellrawbase.replace("%arg%", "stats").replace("%help%", "Show plugin stats").replace("%player%", sender.getName())
                    .replace("%cmd%", commandLabel));

                help_messages.add(
                    "tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"-------------------------------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]".replace(
                        "%player%", sender.getName()));

				if (plugin != null) {
					for (String msg : help_messages) {
						plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), msg);
					}
				}

            } else {
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
            }
        } else {
            sender.sendMessage(
                ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getPluginDescriptionFile().getName() + ChatColor.DARK_AQUA
                    + "----------" + ChatColor.BLUE + "===");

            sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " create <name>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN
                + "Create a new camera path");
            sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " remove <name>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN
                + "Remove a camera path");
            sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " addpoint" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN
                + "Add an point to a camera path");
            sender.sendMessage(
                ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " addcommand <command>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN
                    + "Add an command to a camera path, %player% gets replaced with the player's name");
            sender.sendMessage(
                ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " delpoint [point_number]" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN
                    + "Remove an point from a camera path");
            sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " select <name>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN
                + "Select a camera path by name");
            sender.sendMessage(
                ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " preview <point_number>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN
                    + "Preview a point on the selected camera path");
            sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " info" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN
                + "Info about the currently selected camera path");
            sender.sendMessage(
                ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " setduration <duration>" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN
                    + "Set the total duration of the current camera path");
            sender.sendMessage(
                ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " start [cameraname]" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN
                    + "Run the camera");
            sender.sendMessage(
                ChatColor.BLACK + "[" + ChatColor.RED + "/" + commandLabel + " stop" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Stop the camera");
            sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.GREEN + "/" + commandLabel + " startother <playername> <cameraname>" + ChatColor.BLACK + "] "
                + ChatColor.DARK_GREEN + "Run a camera for a different player");
            sender.sendMessage(
                ChatColor.BLACK + "[" + ChatColor.GREEN + "/" + commandLabel + " stats" + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + "Show plugin stats");

            sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");
        }

        return false;
    }
}
