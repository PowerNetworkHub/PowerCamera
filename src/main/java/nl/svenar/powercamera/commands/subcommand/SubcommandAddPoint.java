package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.PlayerCameraData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandAddPoint extends PowerCameraCommand {

    public SubcommandAddPoint(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerCameraData cameraData = plugin.getPlayerData().get(player);

        if (!sender.hasPermission("powercamera.cmd.addpoint")) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        String easing = "linear";

        if (args.length > 1) {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " addpoint [linear/teleport]");
            return false;
        }

        String cameraName = cameraData.getSelectedCameraId();

        if(args.length == 1) {
            easing = args[0];
            if (!easing.equalsIgnoreCase("linear") && !easing.equalsIgnoreCase("teleport")) {
                sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " addpoint [linear/teleport]");
                return false;
            }
        }

        if (cameraName != null) {
            plugin.getConfigCameras().cameraAddpoint(((Player) sender).getLocation(), easing, cameraName);
            sendMessage(sender, ChatColor.GREEN + "Point added to camera '" + cameraName + "'!");
        } else {
            sendMessage(sender, ChatColor.RED + "No camera selected!");
            sendMessage(sender, ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
        }

        return false;
    }
}
