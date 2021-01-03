package nl.svenar.powercamera.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.PowerCamera;

public class OnJoin implements Listener {

	private PowerCamera plugin;

	public OnJoin(PowerCamera plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerMove(PlayerJoinEvent e) {
		if (this.plugin.getConfigCameras().addPlayer(e.getPlayer().getUniqueId()) || !this.plugin.getConfigPlugin().getConfig().getBoolean("on-join.show-once")) {
			String camera_name = this.plugin.getConfigPlugin().getConfig().getString("on-join.player-camera-path");
			if (camera_name.length() > 0) {
				if (this.plugin.getConfigCameras().camera_exists(camera_name)) {
					new CameraHandler(plugin, e.getPlayer(), camera_name).generatePath().start();
				}
			}
		}
	}
}
