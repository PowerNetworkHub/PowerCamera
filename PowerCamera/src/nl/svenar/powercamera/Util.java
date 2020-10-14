package nl.svenar.powercamera;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Util {

	public static String serializeLocation(Location loc) {
		return loc.getWorld().getUID() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
	}

	public static Location deserializeLocation(String input) {
		String[] input_split = input.split(";");

		UUID world_uid = UUID.fromString(input_split[0]);

		double x = Double.parseDouble(input_split[1]);
		double y = Double.parseDouble(input_split[2]);
		double z = Double.parseDouble(input_split[3]);

		float yaw = Float.parseFloat(input_split[4]);
		float pitch = Float.parseFloat(input_split[5]);

		World world = Bukkit.getServer().getWorld(world_uid);

		return new Location(world, x, y, z, yaw, pitch);
	}
}
