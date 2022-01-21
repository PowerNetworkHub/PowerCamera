package nl.svenar.powercamera.objects;

import java.util.List;

import org.bukkit.Location;

public class CameraPoint {

    String type;
    String easing;
    Double duration;
    Location point_location;
    List<String> commands_start;
    List<String> commands_end;

    public CameraPoint(String type, String easing, Double duration, Location point_location,
            List<String> commands_start, List<String> commands_end) {

        this.type = type;
        this.easing = easing;
        this.duration = duration;
        this.point_location = point_location;
        this.commands_start = commands_start;
        this.commands_end = commands_end;
    }

    public String getType() {
        return type;
    }

    public String getEasing() {
        return easing;
    }

    public Double getDuration() {
        return duration;
    }

    public Location getLocation() {
        return this.point_location;
    }

    public List<String> getStartCommands() {
        return this.commands_start;
    }

    public List<String> getEndCommands() {
        return this.commands_end;
    }

}
