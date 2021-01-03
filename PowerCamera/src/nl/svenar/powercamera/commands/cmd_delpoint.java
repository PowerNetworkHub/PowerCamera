package nl.svenar.powercamera.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.PowerCamera;

public class cmd_delpoint extends PowerCameraCommand {

	public cmd_delpoint(PowerCamera plugin, String command_name) {
		super(plugin, command_name, COMMAND_EXECUTOR.PLAYER);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powercamera.cmd.delpoint")) {
			if (args.length == 0 || args.length == 1) {
				int num = -1;
				if (args.length == 1) {
					num = Integer.parseInt(args[0]) - 1;
				}

				String camera_name = plugin.player_selected_camera.get(((Player) sender).getUniqueId());
				if (camera_name != null) {
					plugin.getConfigCameras().camera_removepoint(camera_name, num);
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Point " + num + " removed from camera '" + camera_name + "'!");
				} else {
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "No camera selected!");
					sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Select a camera by doing: /" + commandLabel + " select <name>");
				}

			} else {
				sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel + " delpoint [point-number]");
			}

		} else {
			sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "You do not have permission to execute this command");
		}

		return false;
	}
}
