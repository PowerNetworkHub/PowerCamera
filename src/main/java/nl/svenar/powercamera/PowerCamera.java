package nl.svenar.powercamera;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import nl.svenar.powercamera.commands.CommandHelp;
import nl.svenar.powercamera.commands.CommandReload;
import nl.svenar.powercamera.commands.CommandCameraInfo;
import nl.svenar.powercamera.commands.CommandCameraList;
import nl.svenar.powercamera.commands.PowerCommand.COMMAND_EXECUTOR;
import nl.svenar.powercamera.configuration.CameraConfig;
import nl.svenar.powercamera.configuration.CoreConfig;
import nl.svenar.powercamera.handlers.PowerCommandHandler;
import nl.svenar.powercamera.metrics.Metrics;
import nl.svenar.powercamera.objects.Camera;
import nl.svenar.powercamera.utils.ServerInfo;
import nl.svenar.powercamera.utils.VersionUtils;

public class PowerCamera extends JavaPlugin {

	private static PowerCamera instance;

	private CoreConfig coreConfig;
    private CameraConfig cameraConfig;

	private ArrayList<Camera> cameras = new ArrayList<Camera>();
	
	public void onEnable() {
		Instant startTime = Instant.now();

		instance = this;

		this.coreConfig = new CoreConfig(this, "config");
		this.cameraConfig = new CameraConfig(this, "cameras");

		this.setCameras(this.getCameraConfig().getCameras());

		// Bukkit.getServer().getPluginManager().registerEvents((Listener) new
		// ChatEvent(this), (Plugin) this);

		Bukkit.getServer().getPluginCommand("powercamera").setExecutor((CommandExecutor) new PowerCommandHandler(this));
		Bukkit.getServer().getPluginCommand("powercamera")
				.setTabCompleter((TabCompleter) new PowerCommandHandler(this));

		// Load all base commands //
		PowerCommandHandler.addPowerCommand("help", new CommandHelp(this, COMMAND_EXECUTOR.ALL, true));
		PowerCommandHandler.addPowerCommand("reload", new CommandReload(this, COMMAND_EXECUTOR.ALL, true));
		PowerCommandHandler.addPowerCommand("list", new CommandCameraList(this, COMMAND_EXECUTOR.ALL, true));
		PowerCommandHandler.addPowerCommand("info", new CommandCameraInfo(this, COMMAND_EXECUTOR.ALL, true));
		// Load all base commands //

		// Register permissions //
		registerPermission("powercamera.command.help");
		registerPermission("powercamera.command.reload");
		registerPermission("powercamera.command.camera.list");
		registerPermission("powercamera.command.camera.info");
		// Register permissions //

		List<String> logoLines = new ArrayList<String>();
		logoLines.add("  ██████   ██████");
		logoLines.add("  ██   ██ ██     ");
		logoLines.add("  ██████  ██     ");
		logoLines.add("  ██      ██     ");
		logoLines.add("  ██       ██████");
		Iterator<String> logoLinesIterator = logoLines.iterator();

		getLogger().info("");
		getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "  " + getDescription().getName()
				+ " v" + getDescription().getVersion());
		getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "  Running on "
				+ ServerInfo.getServerType(getServer()) + " v" + ServerInfo.getServerVersion(getServer()) + " (Java: "
				+ VersionUtils.getJavaVersion() + ")");
		getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "  Startup time: "
				+ Duration.between(startTime, Instant.now()).toMillis() + "ms");
		getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "");
		getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.RED + "  "
				+ (System.getProperty("POWERCAMERARUNNING", "").equals("TRUE")
						? "Reload detected! Why do you hate yourself? :("
						: ""));
		getLogger().info("");

		System.setProperty("POWERCAMERARUNNING", "TRUE");
		System.setProperty("POWERCAMERAENABLED", "TRUE");

		int pluginId = 9107;
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this, pluginId);
	}

	public void onDisable() {
		if (getLogger() != null && this.getDescription() != null) {
			getLogger().info("Disabled " + this.getDescription().getName() + " v" + this.getDescription().getVersion());
		}
	}

	private void registerPermission(String permission) {
		if (Bukkit.getPluginManager().getPermission(permission) != null)
			return;

		Bukkit.getPluginManager().addPermission(new Permission(permission));
	}

	public CoreConfig getCoreConfig() {
        return coreConfig;
    }

    public CameraConfig getCameraConfig() {
        return cameraConfig;
    }


	public void setCameras(ArrayList<Camera> cameras) {
		this.cameras = cameras;
	}

	public Camera getCamera(String name) {
		for (Camera camera : cameras) {
			if (camera.getName().equalsIgnoreCase(name)) {
				return camera;
			}

		}

		return null;
	}

	public ArrayList<Camera> getCameras() {
		return cameras;
	}

	// =================================================================
	// Formatters
	//
	// Default formatters on how PowerCamera looks in the chat
	// in commands and responses.
	// Defined here to keep them uniform for all commands/responses.
	//
	// [PC] {{message}}
	//
	// ===----------PowerCamera-----------===
	// {{message}}
	// ===--------------------------------===
	//
	// ===------PowerCamera | {{title}}-----===
	// {{message}}
	// ===----------------------------------===
	//
	// =================================================================

	public String pluginChatPrefix() {
		return ChatColor.BLACK + "[" + ChatColor.AQUA + "PC" + ChatColor.BLACK + "]" + ChatColor.RESET + " ";
	}

	/**
	 * Get the max length of a single line
	 * 
	 * @return max format line length
	 */
	public int getChatMaxLineLength() {
		return 30;
	}

	/**
	 * Get the default header format for multi-line PowerRanks messages
	 * 
	 * @return formatted header
	 */
	public String getCommandHeader() {
		return getCommandHeader("");
	}

	/**
	 * Get the default header format with title for multi-line PowerRanks messages
	 * 
	 * @param title
	 * @return formatted header
	 */
	public String getCommandHeader(String title) {
		int maxLength = getChatMaxLineLength();
		String text = getDescription().getName() + (title.length() > 0 ? " | " + title : "");

		if (text.length() > maxLength) {
			text = text.substring(0, maxLength - 5) + "...";
		}

		String divider = "";
		for (int i = 0; i < maxLength - text.length(); i++) {
			if (i == (maxLength - text.length()) / 2) {
				divider += text;
			}
			divider += "-";
		}
		return ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + divider + ChatColor.BLUE + "===";
	}

	/**
	 * Get the default footer format for multi-line PowerRanks messages
	 * 
	 * @return formatted footer
	 */
	public String getCommandFooter() {
		int maxLength = getChatMaxLineLength();
		String divider = "";
		for (int i = 0; i < maxLength; i++) {
			divider += "-";
		}
		return ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + divider + ChatColor.BLUE + "===";
	}

	/**
	 * Get the current running PowerRanks instance
	 * 
	 * @return the current PowerRank instance
	 */
	public static PowerCamera getInstance() {
		return instance;
	}
}
