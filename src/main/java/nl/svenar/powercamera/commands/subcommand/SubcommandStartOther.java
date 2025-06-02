package nl.svenar.powercamera.commands.subcommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.commands.structure.CommandExecutionContext;
import nl.svenar.powercamera.data.CameraMode;
import nl.svenar.powercamera.data.PlayerCameraData;

@SuppressWarnings({ "PMD.AvoidLiteralsInIfCondition", "PMD.CommentRequired", "PMD.LocalVariableCouldBeFinal",
        "PMD.MethodArgumentCouldBeFinal" })
public class SubcommandStartOther extends PowerCameraCommand {

    public SubcommandStartOther(PowerCamera plugin, String commandName) {
        super(plugin, commandName, CommandExecutionContext.ALL);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length != 2) {
            sendMessage(sender,
                    ChatColor.DARK_RED + "Usage: /" + commandLabel + " startother <playername> <cameraname>");
            return false;
        }

        String targetName = args[0];
        String cameraName = args[1];

        if (!sender.hasPermission("powercamera.cmd.startother")) {
            sendMessage(sender, ChatColor.DARK_RED + "You do not have permission to execute this command");
            return false;
        }

        List<Player> targetPlayers = getPlayersFromSelector(sender, targetName);

        if (targetPlayers.isEmpty()) {
            sendMessage(sender, ChatColor.DARK_RED + "No matching players found for selector '" + targetName + "'");
            return false;
        }

        for (Player targetPlayer : targetPlayers) {
            PlayerCameraData cameraData = plugin.getPlayerData().get(targetPlayer);

            if (cameraData.getCameraMode() != CameraMode.NONE) {
                sendMessage(sender,
                        ChatColor.DARK_RED + "Player '" + targetPlayer.getName() + "' already has a camera active!");
                continue;
            }

            if (this.plugin.getConfigCameras().cameraExists(cameraName)) {
                cameraData.setCameraHandler(new CameraHandler(plugin, targetPlayer, cameraName).generatePath().start());
                sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Playing '" + cameraName
                        + "' on player: " + targetPlayer.getName());
            } else {
                sendMessage(sender, ChatColor.RED + "Camera '" + cameraName + "' not found!");
                break;
            }
        }

        return false;
    }

    private List<Player> getPlayersFromSelector(CommandSender sender, String selector) {
        selector = selector.replace("@", "").toLowerCase();

        if (selector.equals("a")) {
            // All online players
            return new ArrayList<>(Bukkit.getOnlinePlayers());
        }

        if (selector.equals("r")) {
            // Random online player
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            if (!players.isEmpty()) {
                return List.of(players.get(new Random().nextInt(players.size())));
            }
            return List.of();
        }

        if (selector.equals("p")) {
            // Nearest player to the command block or sender
            if (sender instanceof BlockCommandSender blockSender) {
                Location blockLocation = blockSender.getBlock().getLocation();
                double closestDistance = Double.MAX_VALUE;
                Player closestPlayer = null;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    double distance = player.getLocation().distanceSquared(blockLocation);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPlayer = player;
                    }
                }

                return closestPlayer != null ? List.of(closestPlayer) : List.of();
            }

            if (sender instanceof Player player) {
                return List.of(player);
            }
        }

        Player namedPlayer = Bukkit.getPlayer(selector);
        return namedPlayer != null ? List.of(namedPlayer) : List.of();
    }

}
