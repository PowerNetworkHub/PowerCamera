package nl.svenar.powercamera.commands.subcommand;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.Util;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.PlayerCameraData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.CommentRequired", "PMD.LiteralsFirstInComparisons", "PMD.LocalVariableCouldBeFinal", "PMD.MethodArgumentCouldBeFinal", "PMD.UseStringBufferForStringAppends"})
public class SubcommandInfo extends PowerCameraCommand {

    public SubcommandInfo(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerCameraData cameraData = plugin.getPlayerData().get(player);

        if (!sender.hasPermission("powercamera.cmd.info")) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        if (args.length != 0) {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " info");
            return false;
        }

        String cameraName = cameraData.getSelectedCameraId();
        if (cameraName == null) {
            sendMessage(sender, ChatColor.RED + "No camera selected!");
            sendMessage(sender, ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
            return false;
        }

        List<String> cameraPoints = plugin.getConfigCameras().getPoints(cameraName);
        int cameraDuration = plugin.getConfigCameras().getDuration(cameraName);

        sender.sendMessage(
            ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getPluginDescriptionFile().getName()
                + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
        sender.sendMessage(ChatColor.DARK_GREEN + "Camera name: " + ChatColor.GREEN + cameraName);
        sender.sendMessage(ChatColor.DARK_GREEN + "Path duration: " + ChatColor.GREEN + cameraDuration + " seconds");
        sender.sendMessage(ChatColor.DARK_GREEN + "Camera points (" + ChatColor.GREEN + cameraPoints.size() + ChatColor.DARK_GREEN + "):");

        int index = 0;
        for (String rawPoint : cameraPoints) {
            index++;

            String type = rawPoint.split(":", 3)[0];
            String easing = rawPoint.split(":", 3)[1];
            String point = rawPoint.split(":", 3)[2];

            String pointInfo = "";
            pointInfo += "#" + index + " ";
            pointInfo += type + " (" + easing + "): ";

            if (type.equalsIgnoreCase("location")) {
                Location pointLocation = Util.deserializeLocation(point);

                pointInfo += pointLocation.getWorld().getName();
                pointInfo +=
                    ", (X: " + pointLocation.getBlockX() + ", Y: " + pointLocation.getBlockY() + ", Z: " + pointLocation.getBlockZ() + ")";
                pointInfo += ", (Yaw: " + Math.round(pointLocation.getYaw()) + ", Pitch: " + Math.round(pointLocation.getPitch()) + ")";
            } else {
                pointInfo += point;
            }

            sender.sendMessage(ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + pointInfo);
        }
        sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");

        return false;
    }
}
