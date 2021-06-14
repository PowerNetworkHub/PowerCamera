package nl.svenar.powercamera.events;

import java.util.List;
import java.util.Random;

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
	public void onPlayerJoin(PlayerJoinEvent e) {
		try {
			if (!e.getPlayer().hasPermission("powercamera.bypass.joincamera")) {
				if (this.plugin.getConfigCameras().addPlayer(e.getPlayer().getUniqueId()) || !this.plugin.getConfigPlugin().getConfig().getBoolean("on-join.show-once")) {
					List<String> joinCameras = this.plugin.getConfigPlugin().getConfig().getStringList("on-join.random-player-camera-path");
					Random rand = new Random();
					String camera_name = joinCameras.get(rand.nextInt(joinCameras.size()));
					if (camera_name.length() > 0) {
						if (this.plugin.getConfigCameras().camera_exists(camera_name)) {
							this.plugin.player_camera_handler.put(e.getPlayer().getUniqueId(), new CameraHandler(plugin, e.getPlayer(), camera_name).generatePath().start());
						}
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
