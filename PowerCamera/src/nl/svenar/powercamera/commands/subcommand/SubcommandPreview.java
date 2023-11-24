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

public class SubcommandPreview extends PowerCameraCommand {

    public SubcommandPreview(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerCameraData cameraData = plugin.getPlayerData().get(player);

        if (!sender.hasPermission("powercamera.cmd.preview")) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        if (cameraData.getCameraMode() != CameraMode.NONE) {
            sendMessage(sender, ChatColor.DARK_RED + "Camera already active!");
            return false;
        }

        if (args.length != 1) {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " preview <point-number>");
            return false;
        }

        String cameraName = cameraData.getSelectedCameraId();

        if (cameraName != null) {
            int previewTime = plugin.getConfigPlugin().getConfig().getInt("point-preview-time");
            int num = Integer.parseInt(args[0]) - 1;

            cameraData.setCameraHandler(new CameraHandler(plugin, (Player) sender, cameraName).generatePath().preview((Player) sender, num, previewTime));

        } else {
            sendMessage(sender, ChatColor.RED + "No camera selected!");
            sendMessage(sender, ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
        }

        return false;
    }
}
