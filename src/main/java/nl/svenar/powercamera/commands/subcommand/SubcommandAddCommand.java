package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.PlayerCameraData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"PMD.CommentRequired", "PMD.LocalVariableCouldBeFinal", "PMD.MethodArgumentCouldBeFinal", "PMD.UselessParentheses"})
public class SubcommandAddCommand extends PowerCameraCommand {

    public SubcommandAddCommand(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;

        if (!(sender.hasPermission("powercamera.cmd.addpoint"))) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        if (args.length == 0) {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " addcommand <command>");
            return false;
        }
        
        PlayerCameraData cameraData = plugin.getPlayerData().get(player);
        String cameraName = cameraData.getSelectedCameraId();
        
        if (cameraName != null) {
            String command = String.join(" ", args);
            plugin.getConfigCameras().cameraAddcommand(command, cameraName);
            sendMessage(sender, ChatColor.GREEN + "Command added to camera '" + cameraName + "'!");
        } else {
            sendMessage(sender, ChatColor.RED + "No camera selected!");
            sendMessage(sender, ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
        }

        return false;
    }
}
