package nl.svenar.powercamera.commands.subcommand;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.CommentRequired", "PMD.LocalVariableCouldBeFinal", "PMD.MethodArgumentCouldBeFinal", "PMD.SimpleDateFormatNeedsLocale"})
public class SubcommandStats extends PowerCameraCommand {

    public SubcommandStats(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.ALL);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powercamera.cmd.stats")) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        if (args.length != 0) {
            sendMessage(sender, ChatColor.DARK_RED + "Usage: /" + commandLabel + " stats");
            return false;
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Instant currentTime = Instant.now();

        String invisibilityMode = "None";

        if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode") && this.plugin.getConfigPlugin().getConfig()
            .getBoolean("camera-effects.invisible")) {
            invisibilityMode = "spectator & invisible";
        } else {
            if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode")) {
                invisibilityMode = "specator";
            }

            if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible")) {
                invisibilityMode = "invisible";
            }
        }

        sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getPluginDescriptionFile().getName()
            + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
        sender.sendMessage(ChatColor.DARK_GREEN + "Server version: " + ChatColor.GREEN + Bukkit.getVersion());
        sender.sendMessage(ChatColor.DARK_GREEN + "Bukkit version: " + ChatColor.GREEN + Bukkit.getServer().getBukkitVersion());
        sender.sendMessage(ChatColor.DARK_GREEN + "Java version: " + ChatColor.GREEN + System.getProperty("java.version"));
        sender.sendMessage(
            ChatColor.DARK_GREEN + plugin.getPluginDescriptionFile().getName() + " Version: " + ChatColor.GREEN + plugin.getPluginDescriptionFile()
                .getVersion());
        sender.sendMessage(ChatColor.DARK_GREEN + "Plugin uptime: " + ChatColor.GREEN + format.format(
            Duration.between(plugin.powercameraStartTime, currentTime).toMillis()));
        sender.sendMessage(ChatColor.DARK_GREEN + "Registered cameras: " + ChatColor.GREEN + plugin.getConfigCameras().getCameras().size());
        sender.sendMessage(ChatColor.DARK_GREEN + "Registered players: " + ChatColor.GREEN + plugin.getConfigCameras().getPlayers().size());
        sender.sendMessage(ChatColor.DARK_GREEN + "Invisibility mode: " + ChatColor.GREEN + invisibilityMode);
        sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");

        return false;
    }
}
