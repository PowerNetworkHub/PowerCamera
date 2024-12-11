package nl.svenar.powercamera.config;

import nl.svenar.powercamera.PowerCamera;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Storage for active cameras.
 */
public class ActiveCameraStorage {
    /**
     * The persistent data location type.
     */
    private static final PersistentDataLocation PERSISTENT_DATA_LOCATION = new PersistentDataLocation();

    /**
     * The persistent data game mode type.
     */
    private static final PersistentDataGameMode PERSISTENT_DATA_GAME_MODE = new PersistentDataGameMode();

    /**
     * The persistent data boolean type.
     */
    private static final PersistentDataBoolean PERSISTENT_DATA_BOOLEAN = new PersistentDataBoolean();

    /**
     * The active camera key.
     */
    private final NamespacedKey activeCamera;

    /**
     * The last location key.
     */
    private final NamespacedKey lastLocation;

    /**
     * The last game mode key.
     */
    private final NamespacedKey lastGameMode;

    /**
     * The last visibility key.
     */
    private final NamespacedKey lastVisibility;

    /**
     * Instantiates a new Active camera storage.
     *
     * @param plugin The plugin.
     */
    public ActiveCameraStorage(final PowerCamera plugin) {
        activeCamera = new NamespacedKey(plugin, "activeCamera");
        lastLocation = new NamespacedKey(plugin, "lastLocation");
        lastGameMode = new NamespacedKey(plugin, "lastGameMode");
        lastVisibility = new NamespacedKey(plugin, "lastVisibility");
    }

    /**
     * Set the camera for a player as active.
     *
     * @param cameraName The camera name.
     * @param player     The player.
     */
    public void setCameraActive(final String cameraName, final Player player) {
        final PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();
        persistentDataContainer.set(activeCamera, PersistentDataType.STRING, cameraName);
        if (!persistentDataContainer.has(lastLocation, PERSISTENT_DATA_LOCATION)) {
            persistentDataContainer.set(lastLocation, PERSISTENT_DATA_LOCATION, player.getLocation());
        }
        if (!persistentDataContainer.has(lastGameMode, PERSISTENT_DATA_GAME_MODE)) {
            persistentDataContainer.set(lastGameMode, PERSISTENT_DATA_GAME_MODE, player.getGameMode());
        }
        if (!persistentDataContainer.has(lastVisibility, PERSISTENT_DATA_BOOLEAN)) {
            persistentDataContainer.set(lastVisibility, PERSISTENT_DATA_BOOLEAN, player.isInvisible());
        }
    }

    /**
     * Set the camera for a player as inactive.
     *
     * @param player The player.
     */
    public void setCameraInactive(final Player player, final boolean location, final boolean gameMode, final boolean visibility) {
        final PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();
        persistentDataContainer.remove(activeCamera);
        if (location) {
            final Location locationValue = persistentDataContainer.get(lastLocation, PERSISTENT_DATA_LOCATION);
            if (locationValue != null) {
                player.teleport(locationValue);
            }
        }
        persistentDataContainer.remove(lastLocation);
        if (gameMode) {
            final GameMode gameModeValue = persistentDataContainer.get(lastGameMode, PERSISTENT_DATA_GAME_MODE);
            if (gameModeValue != null) {
                player.setGameMode(gameModeValue);
            }
        }
        persistentDataContainer.remove(lastGameMode);
        if (visibility) {
            final Boolean visibilityValue = persistentDataContainer.get(lastVisibility, PERSISTENT_DATA_BOOLEAN);
            if (visibilityValue != null) {
                player.setInvisible(visibilityValue);
            }
        }
        persistentDataContainer.remove(lastVisibility);
    }

    /**
     * Get the active camera for a player.
     *
     * @param player The player.
     * @return The active camera or null if no camera is active.
     */
    @Nullable
    public String getActiveCamera(final Player player) {
        return player.getPersistentDataContainer().get(activeCamera, PersistentDataType.STRING);
    }

    /**
     * {@link PersistentDataType} for {@link Location}.
     */
    private static final class PersistentDataLocation implements PersistentDataType<String, Location> {

        @Override
        public @NotNull Class<String> getPrimitiveType() {
            return String.class;
        }

        @Override
        public @NotNull Class<Location> getComplexType() {
            return Location.class;
        }

        @Override
        public @NotNull String toPrimitive(@NotNull final Location complex, @NotNull final PersistentDataAdapterContext context) {
            final World world = complex.getWorld();
            final String worldName = world == null ? "" : world.getName();
            return String.format("%s;%s;%s;%s;%s;%s", worldName, complex.getX(), complex.getY(), complex.getZ(), complex.getYaw(), complex.getPitch());
        }

        @Override
        public @NotNull Location fromPrimitive(@NotNull final String primitive, @NotNull final PersistentDataAdapterContext context) {
            final String[] parts = primitive.split(";");

            World world = null;
            final String worldName = parts[0];
            if (worldName != null && !worldName.isEmpty()) {
                world = Bukkit.getWorld(worldName);
                if (world == null) {
                    throw new IllegalArgumentException("unknown world");
                }
            }
            return new Location(world, NumberConversions.toDouble(parts[1]), NumberConversions.toDouble(parts[2]),
                    NumberConversions.toDouble(parts[3]), NumberConversions.toFloat(parts[4]), NumberConversions.toFloat(parts[5]));
        }
    }

    /**
     * {@link PersistentDataType} for {@link GameMode}.
     */
    private static final class PersistentDataGameMode implements PersistentDataType<String, GameMode> {

        @Override
        public @NotNull Class<String> getPrimitiveType() {
            return String.class;
        }

        @Override
        public @NotNull Class<GameMode> getComplexType() {
            return GameMode.class;
        }

        @Override
        public @NotNull String toPrimitive(@NotNull final GameMode complex, @NotNull final PersistentDataAdapterContext context) {
            return complex.name();
        }

        @Override
        public @NotNull GameMode fromPrimitive(@NotNull final String primitive, @NotNull final PersistentDataAdapterContext context) {
            return GameMode.valueOf(primitive);
        }
    }

    /**
     * {@link PersistentDataType} for {@link Boolean}.
     */
    private static final class PersistentDataBoolean implements PersistentDataType<Byte, Boolean> {

        @Override
        public @NotNull Class<Byte> getPrimitiveType() {
            return Byte.class;
        }

        @Override
        public @NotNull Class<Boolean> getComplexType() {
            return Boolean.class;
        }

        @Override
        public @NotNull Byte toPrimitive(@NotNull final Boolean complex, @NotNull final PersistentDataAdapterContext context) {
            return complex ? (byte) 1 : 0;
        }

        @Override
        public @NotNull Boolean fromPrimitive(@NotNull final Byte primitive, @NotNull final PersistentDataAdapterContext context) {
            return primitive == 1;
        }
    }
}
