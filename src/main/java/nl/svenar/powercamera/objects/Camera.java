package nl.svenar.powercamera.objects;

import java.util.ArrayList;

public class Camera {

    private String name = "";
    private ArrayList<CameraPoint> points = new ArrayList<CameraPoint>();

    public Camera(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addPoint(CameraPoint camera_point) {
        this.points.add(camera_point);
    }

    public ArrayList<CameraPoint> getPoints() {
        return this.points;
    }
    
}
