package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.PlayerCameraData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandSelect extends PowerCameraCommand {

    public SubcommandSelect(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerCameraData cameraData = plugin.getPlayerData().get(player);

        if (!sender.hasPermission("powercamera.cmd.select")) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        if (args.length != 1) {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " select <name>");
            return false;
        }

        String cameraName = args[0];
        if (plugin.getConfigCameras().cameraExists(cameraName)) {
            cameraData.setSelectedCameraId(plugin.getConfigCameras().getCameraNameIgnorecase(cameraName));
            sendMessage(sender, ChatColor.GREEN + "Camera '" + cameraName + "' selected!");
        } else {
            sendMessage(sender, ChatColor.RED + "A camera with the name '" + cameraName + "' does not exists!");
        }

        return false;
    }
}
