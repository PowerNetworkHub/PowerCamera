package nl.svenar.powercamera.config;

import java.io.File;
import java.io.IOException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.svenar.powercamera.PowerCamera;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

@SuppressFBWarnings({"CT_CONSTRUCTOR_THROW", "RV_RETURN_VALUE_IGNORED_BAD_PRACTICE"})
@SuppressWarnings({"PMD.AvoidPrintStackTrace", "PMD.CommentRequired", "PMD.MethodArgumentCouldBeFinal"})
public class PluginConfig {

    private File configFile;

    private FileConfiguration config;

    private final PowerCamera plugin;

    public PluginConfig(PowerCamera plugin, String fileName) {
        this.plugin = plugin;

        createConfigFile(fileName);
    }

    private void createConfigFile(String fileName) {
        configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
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
