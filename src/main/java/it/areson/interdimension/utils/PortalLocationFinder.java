package it.areson.interdimension.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PortalLocationFinder {
    private static final Material[] validSpawningMaterial = new Material[]{ Material.AIR, Material.GRASS, Material.TALL_GRASS };

    public static Location findOptimalLocationForPortal(Player player) {
        Location playerLocation = player.getLocation().clone();
        Vector direction = playerLocation.getDirection();
        direction = direction.multiply(new Vector(1, 0, 1)).normalize();
        Location behind = playerLocation.clone().subtract(direction.multiply(2));
        Location testLocation = behind.clone();
        while (!isPortalSpaceAir(testLocation) && testLocation.getY() <= 200) {
            testLocation.add(0, 1, 0);
        }
        if (testLocation.getY() >= 200) {
            testLocation = behind.clone();
            while (!isPortalSpaceAir(testLocation) && testLocation.getY() >= 5) {
                testLocation.subtract(0, 1, 0);
            }
        }
        return testLocation.subtract(0,1,0);
    }

    private static boolean isPortalSpaceAir(Location location) {
        return assertMaterial(location.getBlock().getType(), validSpawningMaterial)
                && assertMaterialInShiftedBlock(location, new Vector(0, 1, 0), validSpawningMaterial)
                && assertMaterialInShiftedBlock(location, new Vector(0, -1, 0), validSpawningMaterial);
    }

    private static boolean assertMaterialInShiftedBlock(Location loc, Vector shifter, Material[] targets) {
        return assertMaterial(loc.clone().add(shifter).getBlock().getType(), targets);
    }

    private static boolean assertMaterial(Material blockToCheck, Material[] targets) {
        for (Material m : targets) {
            if (blockToCheck.equals(m)) {
                return true;
            }
        }
        return false;
    }

}
