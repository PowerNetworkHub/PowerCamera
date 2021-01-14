package nl.svenar.powercamera;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import nl.svenar.powercamera.commands.MainCommand;
import nl.svenar.powercamera.config.CameraStorage;
import nl.svenar.powercamera.config.PluginConfig;
import nl.svenar.powercamera.events.ChatTabExecutor;
import nl.svenar.powercamera.events.OnJoin;
import nl.svenar.powercamera.events.OnMove;
import nl.svenar.powercamera.metrics.Metrics;

public class PowerCamera extends JavaPlugin {

	public String website_url = "https://svenar.nl/powercamera";
	public ArrayList<String> donation_urls = new ArrayList<String>(Arrays.asList("https://ko-fi.com/svenar", "https://patreon.com/svenar"));

	private PluginDescriptionFile pdf;
	private String plugin_chat_prefix = ChatColor.BLACK + "[" + ChatColor.AQUA + "%plugin_name%" + ChatColor.BLACK + "] ";
	private PluginConfig config_plugin;
	private CameraStorage config_cameras;

	public HashMap<UUID, String> player_selected_camera = new HashMap<UUID, String>(); // Selected camera name
	public HashMap<UUID, CAMERA_MODE> player_camera_mode = new HashMap<UUID, CAMERA_MODE>(); // When the player is viewing the camera (/pc start & /pc preview)
	public HashMap<UUID, CameraHandler> player_camera_handler = new HashMap<UUID, CameraHandler>(); // When the player is viewing the camera (/pc start & /pc preview)
	public Instant powercamera_start_time = Instant.now();

	public static enum CAMERA_MODE {
		NONE, PREVIEW, VIEW
	}

	public void onEnable() {
		pdf = this.getDescription();

		plugin_chat_prefix = plugin_chat_prefix.replace("%plugin_name%", pdf.getName());

		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnMove(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnJoin(this), (Plugin) this);
		Bukkit.getServer().getPluginCommand("powercamera").setExecutor((CommandExecutor) new MainCommand(this));
		Bukkit.getServer().getPluginCommand("powercamera").setTabCompleter(new ChatTabExecutor(this));

		setupConfig();

		getLogger().info("Enabled " + getPluginDescriptionFile().getName() + " v" + getPluginDescriptionFile().getVersion());
		getLogger().info("If you'd like to donate, please visit " + donation_urls.get(0) + " or " + donation_urls.get(1));

		int pluginId = 9107;
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this, pluginId);
	}

	public void onDisable() {
		if (getLogger() != null && getPluginDescriptionFile() != null) {
			getLogger().info("Disabled " + getPluginDescriptionFile().getName() + " v" + getPluginDescriptionFile().getVersion());
		}
	}

	public PluginDescriptionFile getPluginDescriptionFile() {
		return this.pdf;
	}

	public String getPluginChatPrefix() {
		return plugin_chat_prefix;
	}

	private void setupConfig() {
		config_plugin = new PluginConfig(this);
		config_cameras = new CameraStorage(this);

		config_plugin.getConfig().set("version", null);
		config_cameras.getConfig().set("version", null);

		if (!config_plugin.getConfig().isSet("camera-effects.spectator-mode"))
			config_plugin.getConfig().set("camera-effects.spectator-mode", true);

		if (!config_plugin.getConfig().isSet("camera-effects.invisible"))
			config_plugin.getConfig().set("camera-effects.invisible", false);

		if (config_plugin.getConfig().isSet("on-new-player-join-camera-path")) {
			ArrayList<String> list = new ArrayList<>();
			list.add(config_plugin.getConfig().getString("on-new-player-join-camera-path"));
			config_plugin.getConfig().set("on-join.random-player-camera-path", list);
			config_plugin.getConfig().set("on-join.show-once", true);
			config_plugin.getConfig().set("on-new-player-join-camera-path", null);
		}

		for (String camera_name : config_cameras.getCameras()) {
			List<String> points = config_cameras.getPoints(camera_name);
			List<String> new_points = new ArrayList<String>();
			for (String point : points) {
				if (!point.startsWith("location:") && !point.startsWith("command:")) {
					point = "location:" + point;
				}
				
				if (point.startsWith("location:") && !(point.startsWith("location:linear:") || point.startsWith("location:teleport:"))) {
					point = point.replaceFirst("location:", "location:linear:");
				}
				
				new_points.add(point);
//				if (point.contains(":")) {
//					new_points.add(point);
//				} else {
//					new_points.add("location:" + point);
//				}
			}
			config_cameras.getConfig().set("cameras." + camera_name + ".points", new_points);
		}

		config_plugin.getConfig().set("version", getPluginDescriptionFile().getVersion());
		config_plugin.saveConfig();

		config_cameras.getConfig().set("version", getPluginDescriptionFile().getVersion());
		config_cameras.saveConfig();
	}

	public PluginConfig getConfigPlugin() {
		return config_plugin;
	}

	public CameraStorage getConfigCameras() {
		return config_cameras;
	}
}
