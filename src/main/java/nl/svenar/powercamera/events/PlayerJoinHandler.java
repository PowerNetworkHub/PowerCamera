package nl.svenar.powercamera.events;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings({"PMD.AvoidCatchingGenericException", "PMD.CommentRequired", "PMD.EmptyCatchBlock", "PMD.LocalVariableCouldBeFinal", "PMD.MethodArgumentCouldBeFinal", "PMD.UnusedPrivateMethod", "PMD.UselessParentheses"})
public class PlayerJoinHandler implements Listener {

    private final PowerCamera plugin;

    public PlayerJoinHandler(PowerCamera plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getPlayerData().handlePlayerJoin(player);

        try {
            if (player.hasPermission("powercamera.bypass.joincamera")) {
                return;
            }

            if (!(this.plugin.getConfigCameras().addPlayer(player.getUniqueId()) || !this.plugin.getConfigPlugin().getConfig()
                .getBoolean("on-join.show-once"))) {
                return;
            }

            List<String> joinCameras = this.plugin.getConfigPlugin().getConfig().getStringList("on-join.random-player-camera-path");
            Random rand = ThreadLocalRandom.current();
            String cameraName = joinCameras.get(rand.nextInt(joinCameras.size()));

            if (!cameraName.isEmpty() && (this.plugin.getConfigCameras().cameraExists(cameraName))) {
                CameraHandler cameraHandler = new CameraHandler(plugin, player, cameraName).generatePath().start();
                plugin.getPlayerData().get(player).setCameraHandler(cameraHandler);
            }


        } catch (Exception ex) {
            // Ignore
        }

        try {
            if (player.isInvisible()) {
                player.setInvisible(false);
            }
        } catch (Exception ex) {
            // Ignore
            // This will be triggered on older versions
        }

    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getPlayerData().handlePlayerQuit(event.getPlayer());
    }
}
