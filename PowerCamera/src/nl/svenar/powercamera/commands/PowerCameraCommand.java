package nl.svenar.powercamera.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;

public abstract class PowerCameraCommand {
	
	protected PowerCamera plugin;
	
	public PowerCameraCommand(PowerCamera plugin, String command_name) {
		MainCommand.add_powercamera_command(command_name, this);
		this.plugin = plugin;
	}
	
	public abstract boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);
}
