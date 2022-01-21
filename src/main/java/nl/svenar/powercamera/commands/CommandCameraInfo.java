package nl.svenar.powercamera.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.objects.Camera;
import nl.svenar.powercamera.objects.CameraPoint;
import nl.svenar.powercamera.utils.PaginationManager;

public class CommandCameraInfo extends PowerCommand {

    public CommandCameraInfo(PowerCamera plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<camera_name>";
    }

    @Override
    public String getDescription() {
        return "Show information about a specific camera";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powercamera.command.camera.info")) {
            sender.sendMessage(
                    plugin.pluginChatPrefix() + ChatColor.RED + "You do not have permission to execute this command.");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + "Please enter a camera name.");
            return false;
        }

        String camera_name = args[0];

        if (this.plugin.getCamera(camera_name) == null) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + "A camera with that name does not exist.");
            return false;
        }

        ArrayList<String> pageData = new ArrayList<String>();
        Camera camera = this.plugin.getCamera(camera_name);

        int point_index = 1;
        for (CameraPoint point : camera.getPoints()) {
            pageData.add(ChatColor.DARK_GREEN + "Point: #" + String.valueOf(point_index));
            pageData.add(ChatColor.DARK_GREEN + "- Type: " + ChatColor.GREEN + point.getType());
            pageData.add(ChatColor.DARK_GREEN + "- Easing: " + ChatColor.GREEN + point.getEasing());
            pageData.add(ChatColor.DARK_GREEN + "- Duration: " + ChatColor.GREEN + point.getDuration());
            pageData.add(ChatColor.DARK_GREEN + "- Location: ");
            pageData.add(ChatColor.DARK_GREEN + "  - World: " + ChatColor.GREEN + point.getLocation().getWorld().getName());
            pageData.add(ChatColor.DARK_GREEN + "  - Position: " + ChatColor.GREEN + "X:" + point.getLocation().getX() + ", " + "Y:" + point.getLocation().getY() + ", " + "Z:" + point.getLocation().getZ());
            pageData.add(ChatColor.DARK_GREEN + "  - Rotation: " + ChatColor.GREEN + "Yaw:" + point.getLocation().getYaw() + ", " + "Pitch:" + point.getLocation().getPitch());
            pageData.add(ChatColor.DARK_GREEN + "- Start commands: " + ChatColor.GREEN + (point.getStartCommands().size() > 0 ? "" : "None"));
            for (String command : point.getStartCommands()) {
                pageData.add(ChatColor.DARK_GREEN + "  - " + ChatColor.GREEN + command);
            }
            pageData.add(ChatColor.DARK_GREEN + "- End commands: " + ChatColor.GREEN + (point.getEndCommands().size() > 0 ? "" : "None"));
            for (String command : point.getEndCommands()) {
                pageData.add(ChatColor.DARK_GREEN + "  - " + ChatColor.GREEN + command);
            }
            point_index++;
        }

        int page = Integer.MIN_VALUE;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]) - 1;

            } catch (NumberFormatException nfe) {
                sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + "Invalid number given as argument.");
            }
        } else {
            page = 0;
        }

        if (page > Integer.MIN_VALUE) {
            PaginationManager paginationManager = new PaginationManager(pageData, camera_name,
                    commandLabel + " info " + camera_name, page, 10);

            paginationManager.send(sender);
        }

        return false;
    }

    @Override
    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> autocomplete = new ArrayList<String>();

        if (args.length == 1) {
            for (Camera camera : plugin.getCameras()) {
                autocomplete.add(camera.getName());
            }
        }

        return autocomplete;
    }
}