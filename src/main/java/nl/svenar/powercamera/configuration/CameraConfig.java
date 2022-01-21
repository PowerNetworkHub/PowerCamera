package nl.svenar.powercamera.configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Location;

import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.handlers.BaseConfig;
import nl.svenar.powercamera.objects.Camera;
import nl.svenar.powercamera.objects.CameraPoint;
import nl.svenar.powercamera.utils.CameraUtils;

public class CameraConfig extends BaseConfig {

        public CameraConfig(PowerCamera plugin, String name) {
                super(plugin, name);
        }

        @SuppressWarnings("unchecked")
        public ArrayList<Camera> getCameras() {
                ArrayList<Camera> cameras = new ArrayList<Camera>();

                for (String name : getConfig().getConfigurationSection("cameras").getKeys(false)) {

                        Camera camera = new Camera(name);

                        for (Object cam : getConfig().getList("cameras." + name + ".points")) {
                                if (cam instanceof LinkedHashMap) {

                                        LinkedHashMap<?, ?> pointData = (LinkedHashMap<?, ?>) ((LinkedHashMap<?, ?>) cam)
                                                        .get("point");

                                        String type = pointData.containsKey("type") ? pointData.get("type").toString()
                                                        : null;
                                        String easing = pointData.containsKey("type")
                                                        ? pointData.get("easing").toString()
                                                        : null;
                                        String duration = pointData.containsKey("type")
                                                        ? pointData.get("duration").toString()
                                                        : null;

                                        String location_world = pointData.containsKey("location")
                                                        && ((LinkedHashMap<?, ?>) pointData
                                                                        .get("location")).containsKey(
                                                                                        "world") ? ((LinkedHashMap<?, ?>) pointData.get("location")).get("world").toString() : null;
                                        String location_x = pointData.containsKey("location")
                                                        && ((LinkedHashMap<?, ?>) pointData
                                                                        .get("location")).containsKey(
                                                                                        "x") ? ((LinkedHashMap<?, ?>) pointData.get("location")).get("x").toString() : null;
                                        String location_y = pointData.containsKey("location")
                                                        && ((LinkedHashMap<?, ?>) pointData
                                                                        .get("location")).containsKey(
                                                                                        "y") ? ((LinkedHashMap<?, ?>) pointData.get("location")).get("y").toString() : null;
                                        String location_z = pointData.containsKey("location")
                                                        && ((LinkedHashMap<?, ?>) pointData
                                                                        .get("location")).containsKey(
                                                                                        "z") ? ((LinkedHashMap<?, ?>) pointData.get("location")).get("z").toString() : null;

                                        String rotation_yaw = pointData.containsKey("rotation")
                                                        && ((LinkedHashMap<?, ?>) pointData
                                                                        .get("rotation")).containsKey(
                                                                                        "yaw") ? ((LinkedHashMap<?, ?>) pointData.get("rotation")).get("yaw").toString() : null;
                                        String rotation_pitch = pointData.containsKey("rotation")
                                                        && ((LinkedHashMap<?, ?>) pointData
                                                                        .get("rotation")).containsKey(
                                                                                        "pitch") ? ((LinkedHashMap<?, ?>) pointData.get("rotation")).get("pitch").toString() : null;

                                        List<String> commands_start = pointData.containsKey("commands")
                                                        && ((LinkedHashMap<?, ?>) pointData
                                                                        .get("commands")).containsKey(
                                                                                        "start") ? ((List<String>) ((LinkedHashMap<?, ?>) pointData.get("commands")).get("start")) : null;
                                        List<String> commands_end = pointData.containsKey("commands")
                                                        && ((LinkedHashMap<?, ?>) pointData
                                                                        .get("commands")).containsKey(
                                                                                "end") ? ((List<String>) ((LinkedHashMap<?, ?>) pointData.get("commands")).get("end")) : null;

                                        Location point_location = CameraUtils.getLocation(location_world,
                                                        Double.parseDouble(location_x), Double.parseDouble(location_y),
                                                        Double.parseDouble(location_z), Float.parseFloat(rotation_yaw),
                                                        Float.parseFloat(rotation_pitch));

                                        CameraPoint camera_point = new CameraPoint(type, easing,
                                                        Double.parseDouble(duration), point_location, commands_start,
                                                        commands_end);

                                        camera.addPoint(camera_point);
                                }
                        }
                        cameras.add(camera);
                }

                return cameras;
        }

        // public final String getNode(final String node) {
        // final String s = getConfig().getString("lang." + language + "." + node);
        // if (s == null) {
        // return "lang." + language + "." + node;
        // }
        // return s.replace("&", String.valueOf(ChatColor.COLOR_CHAR));
        // }

        // public final List<String> getNodeList(final String node) {
        // return getConfig().getStringList("lang." + language + "." +
        // node).parallelStream()
        // .map(s -> s.replace("&",
        // String.valueOf(ChatColor.COLOR_CHAR))).collect(Collectors.toList());
        // }

}
