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
public class SubcommandCreate extends PowerCameraCommand {

    public SubcommandCreate(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerCameraData cameraData = plugin.getPlayerData().get(player);

        if (!sender.hasPermission("powercamera.cmd.create")) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        if (args.length != 1) {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " create <name>");
            return false;
        }

        String cameraName = args[0];

        if (plugin.getConfigCameras().createCamera(cameraName)) {
            sendMessage(sender, ChatColor.GREEN + "Camera '" + cameraName + "' created!");

            cameraData.setSelectedCameraId(plugin.getConfigCameras().getCameraNameIgnorecase(cameraName));
            sendMessage(sender, ChatColor.GREEN + "Camera '" + cameraName + "' selected!");
        } else {
            sendMessage(sender, ChatColor.RED + "A camera with the name '" + cameraName + "' already exists!");
        }

        return false;
    }
}
