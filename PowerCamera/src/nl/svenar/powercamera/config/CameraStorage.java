package nl.svenar.powercamera.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.Util;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CameraStorage {

    private File configFile;
    private FileConfiguration config;

    private final PowerCamera plugin;

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

    public boolean createCamera(String cameraName) {
        if (cameraExists(cameraName)) {
            return false;
        }

        getConfig().set("cameras." + cameraName + ".duration", 10);
        getConfig().set("cameras." + cameraName + ".points", new ArrayList<String>());
        saveConfig();
        return true;
    }

    public boolean removeCamera(String cameraName) {
        if (!cameraExists(cameraName)) {
            return false;
        }

        getConfig().set("cameras." + getCameraNameIgnorecase(cameraName), null);
        saveConfig();
        return true;
    }

    public boolean cameraExists(String cameraName) {
        boolean exists = false;
        for (String cam : getConfig().getConfigurationSection("cameras").getKeys(false)) {
            if (cam.equalsIgnoreCase(cameraName)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public String getCameraNameIgnorecase(String inputName) {
        String cameraName = null;
        for (String cam : getConfig().getConfigurationSection("cameras").getKeys(false)) {
            if (cam.equalsIgnoreCase(inputName)) {
                cameraName = cam;
                break;
            }
        }
        return cameraName;
    }

    public void cameraAddpoint(Location location, String easing, String cameraName) {
        if (!cameraExists(cameraName)) {
            return;
        }

        String newPoint = "location:" + easing + ":" + Util.serializeLocation(location);

        List<String> cameraPoints = getConfig().getStringList("cameras." + getCameraNameIgnorecase(cameraName) + ".points");
        cameraPoints.add(newPoint);

        getConfig().set("cameras." + getCameraNameIgnorecase(cameraName) + ".points", cameraPoints);
        saveConfig();
    }

    public void cameraAddcommand(String command, String cameraName) {
        if (!cameraExists(cameraName)) {
            return;
        }

        String newPoint = "command:" + command;

        List<String> cameraPoints = getConfig().getStringList("cameras." + getCameraNameIgnorecase(cameraName) + ".points");
        cameraPoints.add(newPoint);

        getConfig().set("cameras." + getCameraNameIgnorecase(cameraName) + ".points", cameraPoints);
        saveConfig();
    }

    public void cameraRemovepoint(String cameraName, int num) {
        if (!cameraExists(cameraName)) {
            return;
        }

        List<String> cameraPoints = getConfig().getStringList("cameras." + getCameraNameIgnorecase(cameraName) + ".points");

        if (num < 0) {
            num = 0;
        }

        if (num > cameraPoints.size() - 1) {
            num = cameraPoints.size() - 1;
        }

        if (cameraPoints.size() > 0) {
            if (num == -1) {
                num = cameraPoints.size() - 1;
            }
            cameraPoints.remove(num);

            getConfig().set("cameras." + getCameraNameIgnorecase(cameraName) + ".points", cameraPoints);
            saveConfig();
        }
    }

    public List<String> getPoints(String cameraName) {
        if (!cameraExists(cameraName)) {
            return Collections.emptyList();
        }

        return getConfig().getStringList("cameras." + getCameraNameIgnorecase(cameraName) + ".points");
    }

    public boolean setDuration(String cameraName, int duration) {
        if (!cameraExists(cameraName)) {
            return false;
        }

        getConfig().set("cameras." + getCameraNameIgnorecase(cameraName) + ".duration", duration);
        saveConfig();
        return true;

    }

    public int getDuration(String cameraName) {
        if (!cameraExists(cameraName)) {
            return -1;
        }

        return getConfig().getInt("cameras." + getCameraNameIgnorecase(cameraName) + ".duration");
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