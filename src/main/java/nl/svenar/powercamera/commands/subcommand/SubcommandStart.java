package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.CameraMode;
import nl.svenar.powercamera.data.PlayerCameraData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"PMD.AvoidLiteralsInIfCondition", "PMD.CommentRequired", "PMD.LocalVariableCouldBeFinal", "PMD.MethodArgumentCouldBeFinal", "PMD.UseLocaleWithCaseConversions"})
public class SubcommandStart extends PowerCameraCommand {

    public SubcommandStart(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerCameraData cameraData = plugin.getPlayerData().get(player);

        String cameraName;
        String permission = "powercamera.cmd.start";

        if (args.length == 0) {
            cameraName = cameraData.getSelectedCameraId();
        } else if (args.length == 1) {
            cameraName = args[0];
            permission = permission + "." + cameraName.toLowerCase();
        } else {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " start");
            return false;
        }

        if (!sender.hasPermission(permission)) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        if (cameraData.getCameraMode() != CameraMode.NONE) {
            sendMessage(sender, ChatColor.DARK_RED + "Camera already active!");
            return false;
        }

        if (cameraName == null) {
            sendMessage(sender, ChatColor.RED + "No camera selected!");
            sendMessage(sender, ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
            return false;
        }

        if (!this.plugin.getConfigCameras().cameraExists(cameraName)) {
            sendMessage(sender, ChatColor.RED + "Camera '" + cameraName + "' not found!");
            return false;
        }

        cameraData.setCameraHandler(new CameraHandler(plugin, (Player) sender, cameraName).generatePath().start());
        return false;
    }
}
