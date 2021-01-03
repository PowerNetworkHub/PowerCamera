package nl.svenar.powercamera.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.powercamera.PowerCamera;

public class PluginConfig {

	private File configFile;
	private FileConfiguration config;

	private PowerCamera plugin;

	public PluginConfig(PowerCamera plugin) {
		this.plugin = plugin;

		createConfigFile();
	}

	private void createConfigFile() {
		configFile = new File(plugin.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.saveResource("config.yml", false);
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
}
