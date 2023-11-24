package nl.svenar.powercamera.events;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.data.CameraMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveHandler implements Listener {

    private PowerCamera plugin;

    public PlayerMoveHandler(PowerCamera plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (plugin.playerCameraMode.get(e.getPlayer().getUniqueId()) != null) {
            if (plugin.playerCameraMode.get(e.getPlayer().getUniqueId()) == CameraMode.PREVIEW) {
                e.setCancelled(true);
            }
        }
    }
}
