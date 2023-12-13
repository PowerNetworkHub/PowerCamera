package nl.svenar.powercamera.events;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.data.CameraMode;
import nl.svenar.powercamera.data.PlayerCameraData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveHandler implements Listener {

    private final PowerCamera plugin;

    public PlayerMoveHandler(PowerCamera plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerMove(PlayerMoveEvent event) {
        PlayerCameraData playerCameraData = plugin.getPlayerData().get(event.getPlayer());

        if (playerCameraData.getCameraMode() == CameraMode.PREVIEW) {
            event.setCancelled(true);
        }

    }
}
