package nl.svenar.powercamera.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.objects.Camera;
import nl.svenar.powercamera.utils.PaginationManager;

public class CommandCameraList extends PowerCommand {

    public CommandCameraList(PowerCamera plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "[page]";
    }

    @Override
    public String getDescription() {
        return "List all available camera's";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powercamera.command.camera.list")) {
            sender.sendMessage(
                    plugin.pluginChatPrefix() + ChatColor.RED + "You do not have permission to execute this command.");
            return false;
        }

        ArrayList<String> pageData = new ArrayList<String>();

        for (Camera camera : plugin.getCameras()) {
            pageData.add(ChatColor.BLACK + "[" + ChatColor.DARK_GREEN + camera.getName() + ChatColor.BLACK + "] "
                    + ChatColor.GREEN + String.valueOf(camera.getPoints().size()) + " point(s)");
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
            PaginationManager paginationManager = new PaginationManager(pageData, "list cameras",
                    commandLabel + " list", page, 5);
            
                    paginationManager.setHeaderMessage(ChatColor.DARK_GREEN + "/" + commandLabel + " info <camera_name>" + ChatColor.GREEN + " for more info");
            paginationManager.send(sender);
        }

        return false;
    }

    @Override
    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        return new ArrayList<String>();
    }
}