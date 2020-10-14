package nl.svenar.powercamera;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import nl.svenar.powercamera.PowerCamera.CAMERA_MODE;

public class CameraHandler extends BukkitRunnable {

	private int single_frame_duration_ms = 50;

	private int ticks = 0;

	private PowerCamera plugin;
	private Player player;
	private String camera_name;

	private ArrayList<Location> camera_path_points = new ArrayList<Location>();

	private GameMode previous_gamemode;
	private Location previous_player_location;
//	private boolean previous_invisible;

	public CameraHandler(PowerCamera plugin, Player player, String camera_name) {
		this.plugin = plugin;
		this.player = player;
		this.camera_name = camera_name;
	}

	public CameraHandler generatePath() {
		int max_points = (this.plugin.getConfigCameras().getDuration(this.camera_name) * 1000) / this.single_frame_duration_ms;

		List<String> raw_camera_points = this.plugin.getConfigCameras().getPoints(this.camera_name);

		for (int i = 0; i < raw_camera_points.size() - 1; i++) {
			String raw_point = raw_camera_points.get(i);
			String raw_point_next = raw_camera_points.get(i + 1);

			Location point = Util.deserializeLocation(raw_point);
			Location point_next = Util.deserializeLocation(raw_point_next);

			this.camera_path_points.add(point);
			for (int j = 0; j < max_points / (raw_camera_points.size() - 1) - 1; j++) {
				this.camera_path_points.add(translateLinear(point, point_next, j, max_points / (raw_camera_points.size() - 1) - 1));
			}
		}

		return this;
	}

	private Location translateLinear(Location point, Location point_next, int progress, int progress_max) {
		Location new_point = new Location(point.getWorld(), point.getX(), point.getY(), point.getZ());

		new_point.setX(calculateProgress(point.getX(), point_next.getX(), progress, progress_max));
		new_point.setY(calculateProgress(point.getY(), point_next.getY(), progress, progress_max));
		new_point.setZ(calculateProgress(point.getZ(), point_next.getZ(), progress, progress_max));
		new_point.setYaw((float) calculateProgress(point.getYaw(), point_next.getYaw(), progress, progress_max));
		new_point.setPitch((float) calculateProgress(point.getPitch(), point_next.getPitch(), progress, progress_max));

		return new_point;
	}

	private double calculateProgress(double start, double end, int progress, int progress_max) {
		return start + ((double) progress / (double) progress_max) * (end - start);
	}

	public void start() {
		this.previous_gamemode = this.player.getGameMode();
		this.previous_player_location = this.player.getLocation();
//		this.previous_invisible = this.player.isInvisible();

		player.setGameMode(GameMode.SPECTATOR);
//		player.setInvisible(true);

		runTaskTimer(this.plugin, 1L, 1L);
		this.plugin.player_camera_mode.put(this.player, CAMERA_MODE.VIEW);
		player.teleport(camera_path_points.get(0));

		this.player.sendMessage(this.plugin.getPluginChatPrefix() + ChatColor.GREEN + "Viewing the path of camera '" + this.camera_name + "'!");
	}

	public void stop() {
		plugin.player_camera_mode.put(player, CAMERA_MODE.NONE);
		this.cancel();

		player.teleport(previous_player_location);
		player.setGameMode(previous_gamemode);
//		player.setInvisible(previous_invisible);

		player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "The path of camera '" + camera_name + "' has ended!");
	}
	
	private Vector calculateVelocity(Location start, Location end) {
		return new Vector(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
	}

	@Override
	public void run() {
		if (this.ticks > camera_path_points.size() - 2) {
			this.stop();
			return;
		}

		Location current_pos = camera_path_points.get(this.ticks);
		Location next_point = camera_path_points.get(this.ticks + 1);

		player.teleport(camera_path_points.get(this.ticks));
		
		player.setVelocity(calculateVelocity(current_pos, next_point));

		this.ticks += 1;

	}

}
