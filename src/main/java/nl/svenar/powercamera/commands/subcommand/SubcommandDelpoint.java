package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.PlayerCameraData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"PMD.AvoidLiteralsInIfCondition", "PMD.CommentRequired", "PMD.LocalVariableCouldBeFinal", "PMD.MethodArgumentCouldBeFinal"})
public class SubcommandDelpoint extends PowerCameraCommand {

    public SubcommandDelpoint(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerCameraData cameraData = plugin.getPlayerData().get(player);

        if (!sender.hasPermission("powercamera.cmd.delpoint")) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        if (args.length > 1) {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " delpoint [point-number]");
            return false;
        }

        int num = -1;
        if (args.length == 1) {
            num = Integer.parseInt(args[0]) - 1;
        }

        String cameraName = cameraData.getSelectedCameraId();
        if (cameraName != null) {
            plugin.getConfigCameras().cameraRemovepoint(cameraName, num);
            sendMessage(sender, ChatColor.GREEN + "Point " + num + " removed from camera '" + cameraName + "'!");
        } else {
            sendMessage(sender, ChatColor.RED + "No camera selected!");
            sendMessage(sender, ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
        }

        return false;
    }
}
