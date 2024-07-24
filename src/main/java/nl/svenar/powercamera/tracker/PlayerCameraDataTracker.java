package nl.svenar.powercamera.tracker;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import nl.svenar.powercamera.data.PlayerCameraData;
import org.bukkit.entity.Player;

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
        cameraDataMap.remove(player.getUniqueId());
    }

}
