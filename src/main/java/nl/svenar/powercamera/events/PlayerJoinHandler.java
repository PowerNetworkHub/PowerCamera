package nl.svenar.powercamera.events;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings({"PMD.AvoidCatchingGenericException", "PMD.CommentRequired", "PMD.EmptyCatchBlock", "PMD.LocalVariableCouldBeFinal", "PMD.MethodArgumentCouldBeFinal", "PMD.UnusedPrivateMethod", "PMD.UselessParentheses"})
public class PlayerJoinHandler implements Listener {

    private final PowerCamera plugin;

    public PlayerJoinHandler(PowerCamera plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getPlayerData().handlePlayerJoin(player);

        try {
            String cameraName = getActiveCamera(player);
            if (cameraName == null) {
                cameraName = getJoinCamera(player);
            }

            if (cameraName != null && !cameraName.isEmpty() && (this.plugin.getConfigCameras().cameraExists(cameraName))) {
                CameraHandler cameraHandler = new CameraHandler(plugin, player, cameraName).generatePath().start();
                plugin.getPlayerData().get(player).setCameraHandler(cameraHandler);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }

    private @Nullable String getActiveCamera(final Player player) {
        return this.plugin.getConfigActiveCameras().getActiveCamera(player);
    }

    private @Nullable String getJoinCamera(final Player player) {
        if (player.hasPermission("powercamera.bypass.joincamera")
                || !(this.plugin.getConfigCameras().addPlayer(player.getUniqueId())
                || !this.plugin.getConfigPlugin().getConfig().getBoolean("on-join.show-once"))) {
            return null;
        }

        List<String> joinCameras = this.plugin.getConfigPlugin().getConfig().getStringList("on-join.random-player-camera-path");
        return joinCameras.get(ThreadLocalRandom.current().nextInt(joinCameras.size()));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getPlayerData().handlePlayerQuit(event.getPlayer());
    }
}
