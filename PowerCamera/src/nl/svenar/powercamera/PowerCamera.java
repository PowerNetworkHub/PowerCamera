package nl.svenar.powercamera;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import nl.svenar.powercamera.commands.MainCommand;
import nl.svenar.powercamera.config.CameraStorage;
import nl.svenar.powercamera.config.PluginConfig;
import nl.svenar.powercamera.data.CameraMode;
import nl.svenar.powercamera.events.ChatTabExecutor;
import nl.svenar.powercamera.events.PlayerJoinHandler;
import nl.svenar.powercamera.events.PlayerMoveHandler;
import nl.svenar.powercamera.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PowerCamera extends JavaPlugin {

    public static final String WEBSITE_URL = "https://svenar.nl/powercamera";
    public static final List<String> DONATION_URLS = Arrays.asList("https://ko-fi.com/svenar", "https://patreon.com/svenar");

    public Map<UUID, String> playerSelectedCamera = new HashMap<>(); // Selected camera name
    public Map<UUID, CameraMode> playerCameraMode = new HashMap<>(); // When the player is viewing the camera (/pc start & /pc preview)
    public Map<UUID, CameraHandler> playerCamera_handler = new HashMap<>(); // When the player is viewing the camera (/pc start & /pc preview)
    public Instant powercamera_start_time = Instant.now();
    private PluginDescriptionFile pdf;
    private String plugin_chat_prefix = ChatColor.BLACK + "[" + ChatColor.AQUA + "%plugin_name%" + ChatColor.BLACK + "] ";
    private PluginConfig config_plugin;
    private CameraStorage configCameras;
    private MainCommand mainCommand;

    @Override
    public void onEnable() {
        pdf = this.getDescription();

        plugin_chat_prefix = plugin_chat_prefix.replace("%plugin_name%", pdf.getName());

        registerListeners();
        registerCommand();

        setupConfig();

        getLogger().info("Enabled " + getPluginDescriptionFile().getName() + " v" + getPluginDescriptionFile().getVersion());
        getLogger().info("If you'd like to donate, please visit " + DONATION_URLS.get(0) + " or " + DONATION_URLS.get(1));

        int pluginId = 9107;
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this, pluginId);
    }

    private void registerCommand() {
        this.mainCommand = new MainCommand(this);

        Bukkit.getPluginCommand("powercamera").setExecutor(mainCommand);
        Bukkit.getPluginCommand("powercamera").setTabCompleter(new ChatTabExecutor(this));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerMoveHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinHandler(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled " + pdf.getName() + " v" + pdf.getVersion());
    }

    public PluginDescriptionFile getPluginDescriptionFile() {
        return this.pdf;
    }

    public String getPluginChatPrefix() {
        return plugin_chat_prefix;
    }

    private void setupConfig() {
        config_plugin = new PluginConfig(this, "config.yml");
        configCameras = new CameraStorage(this);

        config_plugin.getConfig().set("version", null);
        configCameras.getConfig().set("version", null);

        if (!config_plugin.getConfig().isSet("camera-effects.spectator-mode")) {
            config_plugin.getConfig().set("camera-effects.spectator-mode", true);
        }

        if (!config_plugin.getConfig().isSet("camera-effects.invisible")) {
            config_plugin.getConfig().set("camera-effects.invisible", false);
        }

        if (config_plugin.getConfig().isSet("on-new-player-join-camera-path")) {
            ArrayList<String> list = new ArrayList<>();
            list.add(config_plugin.getConfig().getString("on-new-player-join-camera-path"));
            config_plugin.getConfig().set("on-join.random-player-camera-path", list);
            config_plugin.getConfig().set("on-join.show-once", true);
            config_plugin.getConfig().set("on-new-player-join-camera-path", null);
        }

        for (String cameraName : configCameras.getCameras()) {
            List<String> points = configCameras.getPoints(cameraName);
            List<String> newPoints = new ArrayList<String>();
            for (String point : points) {
                if (!point.startsWith("location:") && !point.startsWith("command:")) {
                    point = "location:" + point;
                }

                if (point.startsWith("location:") && !(point.startsWith("location:linear:") || point.startsWith("location:teleport:"))) {
                    point = point.replaceFirst("location:", "location:linear:");
                }

                newPoints.add(point);
//				if (point.contains(":")) {
//					newPoints.add(point);
//				} else {
//					newPoints.add("location:" + point);
//				}
            }
            configCameras.getConfig().set("cameras." + cameraName + ".points", newPoints);
        }

        config_plugin.getConfig().set("version", getPluginDescriptionFile().getVersion());
        config_plugin.saveConfig();

        configCameras.getConfig().set("version", getPluginDescriptionFile().getVersion());
        configCameras.saveConfig();
    }

    public PluginConfig getConfigPlugin() {
        return config_plugin;
    }

    public CameraStorage getConfigCameras() {
        return configCameras;
    }

    public MainCommand getMainCommand() {
        return mainCommand;
    }
}
