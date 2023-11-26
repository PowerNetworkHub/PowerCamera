package nl.svenar.powercamera.event;

import nl.svenar.powercamera.CameraHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PowerCameraFinishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final CameraHandler cameraHandler;
    private final Player player;

    public PowerCameraFinishEvent(CameraHandler cameraHandler, Player player) {
        this.cameraHandler = cameraHandler;
        this.player = player;
    }

    public CameraHandler getCameraHandler() {
        return cameraHandler;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
