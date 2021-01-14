package nl.svenar.powercamera.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.Util;

public class CameraStorage {
	private File configFile;
	private FileConfiguration config;

	private PowerCamera plugin;

	public CameraStorage(PowerCamera plugin) {
		this.plugin = plugin;

		createConfigFile();
	}

	private void createConfigFile() {
		configFile = new File(plugin.getDataFolder(), "camera.yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.saveResource("camera.yml", false);
		}

		config = new YamlConfiguration();
		try {
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getConfig() {
		return this.config;
	}

	public void saveConfig() {
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			plugin.getLogger().severe("Error saving " + configFile.getName());
		}
	}

	public boolean create_camera(String camera_name) {
		if (camera_exists(camera_name))
			return false;

		getConfig().set("cameras." + camera_name + ".duration", 10);
		getConfig().set("cameras." + camera_name + ".points", new ArrayList<String>());
		saveConfig();
		return true;
	}

	public boolean remove_camera(String camera_name) {
		if (!camera_exists(camera_name))
			return false;

		getConfig().set("cameras." + get_camera_name_ignorecase(camera_name), null);
		saveConfig();
		return true;
	}

	public boolean camera_exists(String camera_name) {
		boolean exists = false;
		for (String cam : getConfig().getConfigurationSection("cameras").getKeys(false)) {
			if (cam.equalsIgnoreCase(camera_name)) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	public String get_camera_name_ignorecase(String input_name) {
		String camera_name = null;
		for (String cam : getConfig().getConfigurationSection("cameras").getKeys(false)) {
			if (cam.equalsIgnoreCase(input_name)) {
				camera_name = cam;
				break;
			}
		}
		return camera_name;
	}

	public void camera_addpoint(Location location, String easing, String camera_name) {
		if (!camera_exists(camera_name))
			return;

		String new_point = "location:" + easing + ":" + Util.serializeLocation(location);

		List<String> camera_points = getConfig().getStringList("cameras." + get_camera_name_ignorecase(camera_name) + ".points");
		camera_points.add(new_point);

		getConfig().set("cameras." + get_camera_name_ignorecase(camera_name) + ".points", camera_points);
		saveConfig();
	}

	public void camera_addcommand(String command, String camera_name) {
		if (!camera_exists(camera_name))
			return;

		String new_point = "command:" + command;

		List<String> camera_points = getConfig().getStringList("cameras." + get_camera_name_ignorecase(camera_name) + ".points");
		camera_points.add(new_point);

		getConfig().set("cameras." + get_camera_name_ignorecase(camera_name) + ".points", camera_points);
		saveConfig();
	}

	public void camera_removepoint(String camera_name, int num) {
		if (!camera_exists(camera_name))
			return;

		List<String> camera_points = getConfig().getStringList("cameras." + get_camera_name_ignorecase(camera_name) + ".points");

		if (num < 0)
			num = 0;

		if (num > camera_points.size() - 1)
			num = camera_points.size() - 1;

		if (camera_points.size() > 0) {
			if (num == -1)
				num = camera_points.size() - 1;
			camera_points.remove(num);

			getConfig().set("cameras." + get_camera_name_ignorecase(camera_name) + ".points", camera_points);
			saveConfig();
		}
	}

	public List<String> getPoints(String camera_name) {
		if (!camera_exists(camera_name))
			return null;

		return getConfig().getStringList("cameras." + get_camera_name_ignorecase(camera_name) + ".points");
	}

	public boolean setDuration(String camera_name, int duration) {
		if (!camera_exists(camera_name))
			return false;

		getConfig().set("cameras." + get_camera_name_ignorecase(camera_name) + ".duration", duration);
		saveConfig();
		return true;

	}

	public int getDuration(String camera_name) {
		if (!camera_exists(camera_name))
			return -1;

		return getConfig().getInt("cameras." + get_camera_name_ignorecase(camera_name) + ".duration");
	}

	public Set<String> getCameras() {
		return getConfig().getConfigurationSection("cameras").getKeys(false);
	}

	public boolean addPlayer(UUID uuid) {
		List<String> players = getConfig().getStringList("players");

		if (!players.contains(uuid.toString())) {
			players.add(uuid.toString());

			getConfig().set("players", players);
			saveConfig();
			return true;
		}
		return false;
	}

	public List<String> getPlayers() {
		return getConfig().getStringList("players");
	}
}