package nl.svenar.powercamera.events;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener {

    private final PowerCamera plugin;

    public PlayerJoinHandler(PowerCamera plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerJoin(PlayerJoinEvent e) {
        try {
            if (!e.getPlayer().hasPermission("powercamera.bypass.joincamera")) {
                if (this.plugin.getConfigCameras().addPlayer(e.getPlayer().getUniqueId()) || !this.plugin.getConfigPlugin().getConfig()
                    .getBoolean("on-join.show-once")) {
                    List<String> joinCameras = this.plugin.getConfigPlugin().getConfig().getStringList("on-join.random-player-camera-path");
                    Random rand = ThreadLocalRandom.current();
                    String cameraName = joinCameras.get(rand.nextInt(joinCameras.size()));
                    if (!cameraName.isEmpty() && (this.plugin.getConfigCameras().cameraExists(cameraName))) {
                            this.plugin.playerCameraHandler.put(e.getPlayer().getUniqueId(),
                                new CameraHandler(plugin, e.getPlayer(), cameraName).generatePath().start());

                    }
                }
            }
        } catch (Exception ex) {
            // Ignore
        }

        try {
            if (e.getPlayer().isInvisible()) {
                e.getPlayer().setInvisible(false);
            }
        } catch (Exception ex) {
            // Ignore
            // This will be triggered on older versions
        }
    }
}
