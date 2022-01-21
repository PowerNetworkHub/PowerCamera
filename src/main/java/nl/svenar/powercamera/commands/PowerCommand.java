package nl.svenar.powercamera.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;

public abstract class PowerCommand {

	protected PowerCamera plugin;

	public enum COMMAND_EXECUTOR {
		NONE, PLAYER, CONSOLE, COMMANDBLOCK, ALL
	}

	private COMMAND_EXECUTOR ce = COMMAND_EXECUTOR.NONE;
	private HashMap<String, PowerCommand> powerCommands = new HashMap<String, PowerCommand>();
	private boolean showInHelp = false;

	public PowerCommand(PowerCamera plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
		// PowerCommandHandler.addPowerCommand(command_name, this);
		this.plugin = plugin;
		this.ce = ce;
		this.showInHelp = showInHelp;
	}

	public abstract String getArgumentSuggestions();

	public abstract String getDescription();

	public abstract boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);

	public COMMAND_EXECUTOR getCommandExecutor() {
		return this.ce;
	}

	public abstract ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args);

	public HashMap<String, PowerCommand> getSubPowerCommands() {
		return powerCommands;
	}

	public HashMap<String, PowerCommand> getNestedSubPowerCommands() {
		HashMap<String, PowerCommand> nestedPowerCommands = new HashMap<String, PowerCommand>();
		for (Entry<String, PowerCommand> entry : powerCommands.entrySet()) {
			if (entry.getValue().showInHelp()) {
				nestedPowerCommands.put(entry.getKey(), entry.getValue());
				for (Entry<String, PowerCommand> subEntry : entry.getValue().getNestedSubPowerCommands().entrySet()) {
					if (subEntry.getValue().showInHelp()) {
						nestedPowerCommands.put(entry.getKey() + " " + subEntry.getKey(), subEntry.getValue());
					}
				}
			}
		}
		return nestedPowerCommands;
	}

	public PowerCommand getSubPowerCommand(String commandName) {
		return powerCommands.get(commandName);
	}

	public void addSubPowerCommand(String commandName, PowerCommand powerCommand) {
		this.powerCommands.put(commandName, powerCommand);
	}

	public boolean showInHelp() {
		return this.showInHelp;
	}
}
