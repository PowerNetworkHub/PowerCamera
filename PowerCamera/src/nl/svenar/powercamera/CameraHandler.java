package nl.svenar.powercamera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
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
	private HashMap<Integer, ArrayList<String>> camera_path_commands = new HashMap<Integer, ArrayList<String>>();

	private GameMode previous_gamemode;
	private Location previous_player_location;
	private boolean previous_invisible;

	public CameraHandler(PowerCamera plugin, Player player, String camera_name) {
		this.plugin = plugin;
		this.player = player;
		this.camera_name = camera_name;
	}

	public CameraHandler generatePath() {
		int max_points = (this.plugin.getConfigCameras().getDuration(this.camera_name) * 1000) / this.single_frame_duration_ms;

		List<String> raw_camera_points = this.plugin.getConfigCameras().getPoints(this.camera_name);
		List<String> raw_camera_move_points = getMovementPoints(raw_camera_points);

		if (raw_camera_move_points.size() - 1 == 0) {
			for (int j = 0; j < max_points - 1; j++) {
				this.camera_path_points.add(Util.deserializeLocation(raw_camera_move_points.get(0).split(":", 2)[1]));
			}
		} else {
			for (int i = 0; i < raw_camera_move_points.size() - 1; i++) {
				String raw_point = raw_camera_move_points.get(i).split(":", 2)[1];
				String raw_point_next = raw_camera_move_points.get(i + 1).split(":", 2)[1];
				String easing = raw_camera_move_points.get(i + 1).split(":", 2)[0];

				Location point = Util.deserializeLocation(raw_point);
				Location point_next = Util.deserializeLocation(raw_point_next);

				this.camera_path_points.add(point);
				for (int j = 0; j < max_points / (raw_camera_move_points.size() - 1) - 1; j++) {
					if (easing.equalsIgnoreCase("linear")) {
						this.camera_path_points.add(translateLinear(point, point_next, j, max_points / (raw_camera_move_points.size() - 1) - 1));
					}
					if (easing.equalsIgnoreCase("teleport")) {
						this.camera_path_points.add(point_next);
					}
				}
			}
		}

		int command_index = 0;
		for (String raw_point : raw_camera_points) {
			String type = raw_point.split(":", 3)[0];
//			String easing = raw_point.split(":", 3)[1];
			String data = raw_point.split(":", (type == "location" ? 3 : 2))[type == "location" ? 2 : 1];

			if (type.equalsIgnoreCase("location")) {
				command_index += 1;
			}

			if (type.equalsIgnoreCase("command")) {
				int index = ((command_index) * max_points / (raw_camera_move_points.size()) - 1);
				index = command_index == 0 ? 0 : index - 1;
				index = index < 0 ? 0 : index;
				if (!this.camera_path_commands.containsKey(index))
					this.camera_path_commands.put(index, new ArrayList<String>());
				this.camera_path_commands.get(index).add(data);
//				this.camera_path_commands.put(index, raw_camera_points.get(0));
			}
		}

		return this;
	}

	private List<String> getMovementPoints(List<String> raw_camera_points) {
		List<String> output = new ArrayList<String>();
		for (String raw_point : raw_camera_points) {
			String[] point_data = raw_point.split(":", 2);
			if (point_data[0].equalsIgnoreCase("location")) {
				output.add(point_data[1]);
			}
		}
		return output;
	}

	private Location translateLinear(Location point, Location point_next, int progress, int progress_max) {
		if (!point.getWorld().getUID().toString().equals(point_next.getWorld().getUID().toString())) {
			return point_next;
		}

		Location new_point = new Location(point_next.getWorld(), point.getX(), point.getY(), point.getZ());

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

	public CameraHandler start() {
		this.previous_gamemode = this.player.getGameMode();
		this.previous_player_location = this.player.getLocation();
		this.previous_invisible = Util.isPlayerInvisible(this.player);

		if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode"))
			player.setGameMode(GameMode.SPECTATOR);
		if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible"))
			player.setInvisible(true);

		this.plugin.player_camera_mode.put(this.player.getUniqueId(), CAMERA_MODE.VIEW);
		runTaskTimer(this.plugin, 1L, 1L);
		if (camera_path_points.size() > 0) {
			player.teleport(camera_path_points.get(0));
		}

		if (!this.player.hasPermission("powercamera.hidestartmessages"))
			this.player.sendMessage(this.plugin.getPluginChatPrefix() + ChatColor.GREEN + "Viewing the path of camera '" + this.camera_name + "'!");
		return this;
	}

	public CameraHandler stop() {
		plugin.player_camera_mode.put(player.getUniqueId(), CAMERA_MODE.NONE);
		try {
			this.cancel();
		} catch (Exception e) {
		}

		player.teleport(previous_player_location);
		if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode"))
			player.setGameMode(previous_gamemode);
		if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible"))
			player.setInvisible(previous_invisible);

		if (!this.player.hasPermission("powercamera.hidestartmessages"))
			player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "The path of camera '" + camera_name + "' has ended!");
		return this;
	}

	private Vector calculateVelocity(Location start, Location end) {
		return new Vector(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
	}

	@Override
	public void run() {
		if (plugin.player_camera_mode.get(player.getUniqueId()) == PowerCamera.CAMERA_MODE.VIEW) {
			if (this.ticks > camera_path_points.size() - 2) {
				this.stop();
				return;
			}

			Location current_pos = camera_path_points.get(this.ticks);
			Location next_point = camera_path_points.get(this.ticks + 1);

			player.teleport(camera_path_points.get(this.ticks));

			if (camera_path_commands.containsKey(this.ticks)) {
				for (String cmd : camera_path_commands.get(this.ticks)) {
					String command = cmd.replaceAll("%player%", player.getName());
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
				}
			}

			player.setVelocity(calculateVelocity(current_pos, next_point));

			this.ticks += 1;
		} else {
			if (plugin.player_camera_mode.get(player.getUniqueId()) == PowerCamera.CAMERA_MODE.NONE)
				return;
			player.teleport(previous_player_location);
			if (plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode"))
				player.setGameMode(previous_gamemode);
			if (plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible"))
				player.setInvisible(previous_invisible);
			plugin.player_camera_mode.put(player.getUniqueId(), PowerCamera.CAMERA_MODE.NONE);
			player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview ended!");
		}

	}

	public CameraHandler preview(Player player, int num, int preview_time) {
		List<String> camera_points = plugin.getConfigCameras().getPoints(camera_name);

		if (num < 0)
			num = 0;

		if (num > camera_points.size() - 1)
			num = camera_points.size() - 1;

		if (!camera_points.get(num).split(":", 2)[0].equalsIgnoreCase("location")) {
			player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "Point " + (num + 1) + " is not a location!");
			return this;
		}

		player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview started of point " + (num + 1) + "!");
		player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Ending in " + preview_time + " seconds.");

		previous_gamemode = player.getGameMode();
		previous_player_location = player.getLocation();
		Location point = Util.deserializeLocation(camera_points.get(num).split(":", 3)[2]);
		previous_invisible = player.isInvisible();

		plugin.player_camera_mode.put(player.getUniqueId(), PowerCamera.CAMERA_MODE.PREVIEW);
		if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode"))
			player.setGameMode(GameMode.SPECTATOR);
		if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible"))
			player.setInvisible(true);
		player.teleport(point);

		runTaskLater(this.plugin, preview_time * 20);
		return this;
	}

}
