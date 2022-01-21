package nl.svenar.powercamera.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;

public class CommandReload extends PowerCommand {

    public CommandReload(PowerCamera plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Reload the configuration files";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powercamera.command.reload")) {
            sender.sendMessage(
                    plugin.pluginChatPrefix() + ChatColor.RED + "You do not have permission to execute this command.");
            return false;
        }

        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + "Reloading the plugin configuration file...");
        this.plugin.getCoreConfig().load();
        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + "Reloaded the plugin configuration file!");

        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + "Reloading the camera configuration file...");
        this.plugin.getCameraConfig().load();
        this.plugin.setCameras(this.plugin.getCameraConfig().getCameras());
        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + "Reloaded the camera configuration file!");

        return false;
    }

    @Override
    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        return new ArrayList<String>();
    }
}