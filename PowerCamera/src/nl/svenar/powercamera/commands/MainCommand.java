package nl.svenar.powercamera.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.subcommand.SubcommandAddCommand;
import nl.svenar.powercamera.commands.subcommand.SubcommandAddPoint;
import nl.svenar.powercamera.commands.subcommand.SubcommandCreate;
import nl.svenar.powercamera.commands.subcommand.SubcommandDelpoint;
import nl.svenar.powercamera.commands.subcommand.SubcommandHelp;
import nl.svenar.powercamera.commands.subcommand.SubcommandInfo;
import nl.svenar.powercamera.commands.subcommand.SubcommandInvisible;
import nl.svenar.powercamera.commands.subcommand.SubcommandPreview;
import nl.svenar.powercamera.commands.subcommand.SubcommandRemove;
import nl.svenar.powercamera.commands.subcommand.SubcommandSelect;
import nl.svenar.powercamera.commands.subcommand.SubcommandSetDuration;
import nl.svenar.powercamera.commands.subcommand.SubcommandStart;
import nl.svenar.powercamera.commands.subcommand.SubcommandStartOther;
import nl.svenar.powercamera.commands.subcommand.SubcommandStats;
import nl.svenar.powercamera.commands.subcommand.SubcommandStop;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    private final Map<String, PowerCameraCommand> powercamera_commands = new HashMap<String, PowerCameraCommand>();

    private final PowerCamera plugin;

    public MainCommand(PowerCamera plugin) {
        this.plugin = plugin;

        registerSubcommand(new SubcommandHelp(plugin, "help"));
        registerSubcommand(new SubcommandCreate(plugin, "create"));
        registerSubcommand(new SubcommandRemove(plugin, "remove"));
        registerSubcommand(new SubcommandAddPoint(plugin, "addpoint"));
        registerSubcommand(new SubcommandAddCommand(plugin, "addcommand"));
        registerSubcommand(new SubcommandDelpoint(plugin, "delpoint"));
        registerSubcommand(new SubcommandSelect(plugin, "select"));
        registerSubcommand(new SubcommandPreview(plugin, "preview"));
        registerSubcommand(new SubcommandInfo(plugin, "info"));
        registerSubcommand(new SubcommandSetDuration(plugin, "setduration"));
        registerSubcommand(new SubcommandStart(plugin, "start"));
        registerSubcommand(new SubcommandStartOther(plugin, "startother"));
        registerSubcommand(new SubcommandStop(plugin, "stop"));
        registerSubcommand(new SubcommandStats(plugin, "stats"));
        registerSubcommand(new SubcommandInvisible(plugin, "invisible"));
    }

    public PowerCameraCommand get_powercamera_command(String command_name) {
        return powercamera_commands.get(command_name.toLowerCase());
    }

    private void registerSubcommand(PowerCameraCommand command_handler) {
        powercamera_commands.put(command_handler.getCommand_name().toLowerCase(Locale.ROOT), command_handler);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(
                ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getPluginDescriptionFile().getName() + ChatColor.DARK_AQUA
                    + "----------" + ChatColor.BLUE + "===");
            sender.sendMessage(ChatColor.GREEN + "/" + commandLabel + " help" + ChatColor.DARK_GREEN + " - For the command list.");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + plugin.getPluginDescriptionFile().getAuthors().get(0));
            sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + plugin.getPluginDescriptionFile().getVersion());
            sender.sendMessage(ChatColor.DARK_GREEN + "Website: " + ChatColor.GREEN + plugin.WEBSITE_URL);
            sender.sendMessage(ChatColor.DARK_GREEN + "Support me: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
            sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");
            return false;
        }

        String command = args[0];
        PowerCameraCommand command_handler = get_powercamera_command(command);

        if (command_handler == null) {
            sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Unknown Command");
            return false;
        }

        boolean is_allowed = command_handler.getCommandExecutor().isAllowed(sender);

        if (is_allowed) {
            return command_handler.onCommand(sender, cmd, commandLabel, Arrays.copyOfRange(args, 1, args.length));
        }

        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.DARK_RED + "Only players can use this command");
        return false;
    }
}
