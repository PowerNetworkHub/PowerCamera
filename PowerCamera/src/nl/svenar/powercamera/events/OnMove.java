package nl.svenar.powercamera.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import nl.svenar.powercamera.PowerCamera;

public class OnMove implements Listener {

	private PowerCamera plugin;

	public OnMove(PowerCamera plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerMove(PlayerMoveEvent e) {
		if (plugin.player_camera_mode.get(e.getPlayer().getUniqueId()) != null) {
			if (plugin.player_camera_mode.get(e.getPlayer().getUniqueId()) == PowerCamera.CAMERA_MODE.PREVIEW) {
				e.setCancelled(true);
			}
		}
	}
}
