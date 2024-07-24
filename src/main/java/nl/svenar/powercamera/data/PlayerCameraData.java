package nl.svenar.powercamera.data;

import java.util.UUID;
import nl.svenar.powercamera.CameraHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings({"PMD.CommentRequired", "PMD.DataClass", "PMD.MethodArgumentCouldBeFinal"})
public class PlayerCameraData {

    private final UUID playerId;

    private String selectedCameraId;
    private CameraMode cameraMode = CameraMode.NONE;
    private CameraHandler cameraHandler;

    public PlayerCameraData(UUID playerId) {
        this.playerId = playerId;
    }

    // Getter method

    public Player getPlayer() {
        return Bukkit.getPlayer(playerId);
    }

    // GETTERS

    public UUID getPlayerId() {
        return playerId;
    }

    public String getSelectedCameraId() {
        return selectedCameraId;
    }

    public CameraMode getCameraMode() {
        return cameraMode;
    }

    public CameraHandler getCameraHandler() {
        return cameraHandler;
    }

    // SETTERS


    public void setSelectedCameraId(String selectedCameraId) {
        this.selectedCameraId = selectedCameraId;
    }

    public void setCameraMode(CameraMode cameraMode) {
        this.cameraMode = cameraMode;
    }

    public void setCameraHandler(CameraHandler cameraHandler) {
        this.cameraHandler = cameraHandler;
    }
}
