package it.areson.interdimension.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PortalLocationFinder {

    public static Location findOptimalLocationForPortal(Player player) {
        Location playerLocation = player.getLocation().clone();
        Vector direction = playerLocation.getDirection();
        direction = direction.multiply(new Vector(1, 0, 1));
        direction = direction.normalize();
        Location behind = playerLocation.clone().subtract(direction.multiply(2));
        Location testLocation = behind.clone();
        while (!isPortalSpaceAir(testLocation) &&
                testLocation.getY() <= 200) {
            testLocation.add(0, 1, 0);
        }
        if (testLocation.getY() >= 200) {
            testLocation = behind.clone();
            while (!isPortalSpaceAir(testLocation) && testLocation.getY() >= 5) {
                testLocation.subtract(0, 1, 0);
            }
        }
        return testLocation;
    }

    public static boolean isPortalSpaceAir(Location location) {
        return location.getBlock().getType().equals(Material.AIR) &&
                location.clone().subtract(0, 1, 0).getBlock().getType().equals(Material.AIR) &&
                location.clone().add(0, 1, 0).getBlock().getType().equals(Material.AIR);

    }

}
