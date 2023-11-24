package nl.svenar.powercamera.commands.subcommand;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubcommandInvisible extends PowerCameraCommand {

    public SubcommandInvisible(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (sender.hasPermission("powercamera.cmd.invisible")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false")) {
                    boolean set_invisible = args[0].equalsIgnoreCase("true");
                    player.setInvisible(set_invisible);
                } else {
                    sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel
                        + " invisible <true/false>");
                }
            } else {
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Usage: /" + commandLabel
                    + " invisible <true/false>");
            }
        } else {
            sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED
                + "You do not have permission to execute this command");
        }

        return false;
    }
}
