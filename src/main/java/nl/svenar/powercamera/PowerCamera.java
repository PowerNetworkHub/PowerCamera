package nl.svenar.powercamera;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.svenar.powercamera.commands.MainCommand;
import nl.svenar.powercamera.config.CameraStorage;
import nl.svenar.powercamera.config.PluginConfig;
import nl.svenar.powercamera.events.ChatTabExecutor;
import nl.svenar.powercamera.events.PlayerJoinHandler;
import nl.svenar.powercamera.events.PlayerMoveHandler;
import nl.svenar.powercamera.metrics.Metrics;
import nl.svenar.powercamera.tracker.PlayerCameraDataTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressFBWarnings({"DLS_DEAD_LOCAL_STORE", "MS_MUTABLE_COLLECTION_PKGPROTECT", "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"})
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.AvoidDuplicateLiterals", "PMD.AvoidReassigningLoopVariables", "PMD.CognitiveComplexity", "PMD.CommentRequired", "PMD.CyclomaticComplexity", "PMD.LocalVariableCouldBeFinal", "PMD.LooseCoupling", "PMD.UnnecessaryImport", "PMD.UseDiamondOperator"})
public class PowerCamera extends JavaPlugin {

    public static final String WEBSITE_URL = "https://svenar.nl/powercamera";
    public static final List<String> DONATION_URLS = Arrays.asList("https://ko-fi.com/svenar", "https://patreon.com/svenar");

    private PlayerCameraDataTracker playerCameraDataTracker;

    // public Map<UUID, String> playerSelectedCamera = new HashMap<>(); // Selected camera name
    // public Map<UUID, CameraMode> playerCameraMode = new HashMap<>(); // When the player is viewing the camera (/pc start & /pc preview)
    // public Map<UUID, CameraHandler> playerCameraHandler = new HashMap<>(); // When the player is viewing the camera (/pc start & /pc preview)
    public Instant powercameraStartTime = Instant.now();
    private PluginDescriptionFile pdf;
    private String pluginChatPrefix = ChatColor.BLACK + "[" + ChatColor.AQUA + "%pluginName%" + ChatColor.BLACK + "] ";
    private PluginConfig configPlugin;
    private CameraStorage configCameras;
    private MainCommand mainCommand;

    @Override
    public void onEnable() {
        pdf = this.getDescription();

        pluginChatPrefix = pluginChatPrefix.replace("%pluginName%", pdf.getName());

        this.playerCameraDataTracker = new PlayerCameraDataTracker();

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
        return pluginChatPrefix;
    }

    private void setupConfig() {
        configPlugin = new PluginConfig(this, "config.yml");
        configCameras = new CameraStorage(this);

        configPlugin.getConfig().set("version", null);
        configCameras.getConfig().set("version", null);

        if (!configPlugin.getConfig().isSet("camera-effects.spectator-mode")) {
            configPlugin.getConfig().set("camera-effects.spectator-mode", true);
        }

        if (!configPlugin.getConfig().isSet("camera-effects.invisible")) {
            configPlugin.getConfig().set("camera-effects.invisible", false);
        }

        if (configPlugin.getConfig().isSet("on-new-player-join-camera-path")) {
            ArrayList<String> list = new ArrayList<>();
            list.add(configPlugin.getConfig().getString("on-new-player-join-camera-path"));
            configPlugin.getConfig().set("on-join.random-player-camera-path", list);
            configPlugin.getConfig().set("on-join.show-once", true);
            configPlugin.getConfig().set("on-new-player-join-camera-path", null);
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

        configPlugin.getConfig().set("version", getPluginDescriptionFile().getVersion());
        configPlugin.saveConfig();

        configCameras.getConfig().set("version", getPluginDescriptionFile().getVersion());
        configCameras.saveConfig();
    }

    public PluginConfig getConfigPlugin() {
        return configPlugin;
    }

    public CameraStorage getConfigCameras() {
        return configCameras;
    }

    public MainCommand getMainCommand() {
        return mainCommand;
    }

    public PlayerCameraDataTracker getPlayerData() {
        return playerCameraDataTracker;
    }
}
