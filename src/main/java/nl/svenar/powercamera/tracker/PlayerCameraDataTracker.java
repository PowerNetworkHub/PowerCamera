package nl.svenar.powercamera.tracker;

import nl.svenar.powercamera.CameraHandler;
import nl.svenar.powercamera.data.PlayerCameraData;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.CommentRequired", "PMD.MethodArgumentCouldBeFinal"})
public class PlayerCameraDataTracker {

    private final Map<UUID, PlayerCameraData> cameraDataMap = new ConcurrentHashMap<>();

    public PlayerCameraData get(UUID playerId) {
        return cameraDataMap.get(playerId);
    }

    public PlayerCameraData get(Player player) {
        return get(player.getUniqueId());
    }

    public void handlePlayerJoin(Player player) {
        cameraDataMap.put(player.getUniqueId(), new PlayerCameraData(player.getUniqueId()));
    }

    public void handlePlayerQuit(Player player) {
        final CameraHandler cameraHandler = cameraDataMap.remove(player.getUniqueId()).getCameraHandler();
        if (cameraHandler != null) {
            cameraHandler.cancel();
        }
    }
}
