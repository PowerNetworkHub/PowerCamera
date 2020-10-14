package nl.svenar.powercamera;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
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
	
	public HashMap<Player, String> player_selected_camera = new HashMap<Player, String>(); // Selected camera name
	public HashMap<Player, CAMERA_MODE> player_camera_mode = new HashMap<Player, CAMERA_MODE>(); // When the player is viewing the camera (/pc start & /pc preview)
	public Instant powercamera_start_time = Instant.now();
	
	public static enum CAMERA_MODE {
		NONE,
		PREVIEW,
		VIEW
	}

	public void onEnable() {
		pdf = this.getDescription();
		
		plugin_chat_prefix = plugin_chat_prefix.replace("%plugin_name%", pdf.getName());
		
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnMove(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnJoin(this), (Plugin) this);
		Bukkit.getServer().getPluginCommand("powercamera").setExecutor((CommandExecutor) new MainCommand(this));
		Bukkit.getServer().getPluginCommand("powercamera").setTabCompleter(new ChatTabExecutor(this));
		
		config_plugin = new PluginConfig(this);
		config_cameras = new CameraStorage(this);
		
		config_plugin.getConfig().set("version", getPluginDescriptionFile().getVersion());
		config_plugin.saveConfig();
		config_cameras.getConfig().set("version", getPluginDescriptionFile().getVersion());
		config_cameras.saveConfig();
		
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

	public PluginConfig getConfigPlugin() {
		return config_plugin;
	}
	
	public CameraStorage getConfigCameras() {
		return config_cameras;
	}
}
