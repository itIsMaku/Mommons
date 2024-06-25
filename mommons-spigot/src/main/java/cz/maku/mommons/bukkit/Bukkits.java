package cz.maku.mommons.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class Bukkits {

    public static char LOCATION_SEPARATOR = ';';

    public static String locationToString(Location location) {
        return String.format("%s%s%s%s%s%s%s%s%s%s%s", location.getWorld().getName(), LOCATION_SEPARATOR, location.getX(), LOCATION_SEPARATOR, location.getY(), LOCATION_SEPARATOR, location.getZ(), LOCATION_SEPARATOR, location.getYaw(), LOCATION_SEPARATOR, location.getPitch());
    }

    public static Location stringToLocation(String string) {
        String[] split = string.split(String.valueOf(LOCATION_SEPARATOR));
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

}
