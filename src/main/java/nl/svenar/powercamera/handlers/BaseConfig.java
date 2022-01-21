package nl.svenar.powercamera.handlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.powercamera.PowerCamera;

/**
 * Base class to be used for configuration files
 *
 * @author Sven Arends
 */
public abstract class BaseConfig {

    protected File file;
    protected FileConfiguration config;
    protected String name;
    protected PowerCamera plugin;

    /**
     * Constructor to initialize and load a config file. New configurations use this
     * class as base
     *
     * @param (plugin) PowerCamera instance
     * @param (name)   The name of the file to copy from the jar file to the
     *                 PowerCamera directory
     */
    protected BaseConfig(PowerCamera plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.file = new File(plugin.getDataFolder(), this.name + ".yml");
        copyConfig();
        load();
    }

    /**
     * Get the configuration file associated with this base config
     *
     * @return FileConfiguration (this config file)
     */
    public final FileConfiguration getConfig() {
        if (this.config == null) {
            load();
        }

        return this.config;
    }

    /**
     * Load the configuration file from disk into this BaseConfig instance. Get the
     * config afterwards by calling getConfig() of this particular instance.
     */
    public final void load() {
        this.file = new File(plugin.getDataFolder(), this.name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Copy the cofig from the compiled jar file to the filesystem
     * If the file already exists on the filesystem, check If
     * the amount of keys differ, if so, delete the config and
     * re-copy it from the jar to the filesystem.
     */
    private void copyConfig() {
        if (this.file.exists()) {
            InputStream stream = this.plugin.getResource(this.name + ".yml");

            FileConfiguration tmpConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));

            if (getConfig().getKeys(true).containsAll(tmpConfig.getKeys(true))) {
                return;
            }
        }

        try {
            this.plugin.saveResource(this.name + ".yml", true);
        } catch (IllegalArgumentException e) {
            // if (plugin.getLangConfig() != null) {
            //     plugin.getLogger().severe(plugin.getLangConfig().getNode("error.noconfigfound"));
            // } else {
            //     plugin.getLogger().severe("error.noconfigfound");
            // }
        }
    }

    /**
     * Remove all keys from the config file
     */
    public void clearFile() {
        getConfig().getKeys(false).forEach(key -> getConfig().set(key, null));
    }

    /**
     * Save the configuration to the filesystem
     */
    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
