package nl.svenar.powercamera.config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.svenar.powercamera.PowerCamera;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

@SuppressFBWarnings({"CT_CONSTRUCTOR_THROW", "RV_RETURN_VALUE_IGNORED_BAD_PRACTICE"})
@SuppressWarnings({"PMD.AvoidPrintStackTrace", "PMD.CommentRequired", "PMD.MethodArgumentCouldBeFinal"})
public class PluginConfig {

    protected final PowerCamera plugin;

    protected final File configFile;

    protected final FileConfiguration config;

    public PluginConfig(PowerCamera plugin, String fileName) {
        this.plugin = plugin;

        configFile = createConfigFile(fileName);
        config = createConfig(configFile);
    }

    private File createConfigFile(final String fileName) {
        final File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (plugin.getResource(fileName) == null) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "Error creating " + file.getName(), e);
                }
            } else {
                plugin.saveResource(fileName, false);
            }
        }
        return file;
    }

    private FileConfiguration createConfig(File file) {
        final FileConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Error loading " + file.getName(), e);
        }
        return config;
    }

    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error saving " + configFile.getName(), e);
        }
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
