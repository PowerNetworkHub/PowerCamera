package nl.svenar.powercamera.event;

import nl.svenar.powercamera.CameraHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings({"PMD.CallSuperInConstructor", "PMD.CommentRequired", "PMD.FieldNamingConventions", "PMD.MethodArgumentCouldBeFinal"})
public class PowerCameraFinishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final CameraHandler cameraHandler;

    public PowerCameraFinishEvent(CameraHandler cameraHandler) {
        this.cameraHandler = cameraHandler;
    }

    public CameraHandler getCameraHandler() {
        return cameraHandler;
    }

    public Player getPlayer() {
        return cameraHandler.getPlayer();
    }

    public String getCameraName() {
        return cameraHandler.getCameraName();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
