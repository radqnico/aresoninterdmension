package it.areson.interdimension.locationfinder;

import it.areson.interdimension.Configuration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Tool class with static methods that find a suitable location for a portal to spawn at.
 */
public class LocationFinder {

    private static final Material[] validSpawningMaterial = new Material[]{Material.AIR, Material.GRASS, Material.TORCH, Material.TALL_GRASS, Material.CAVE_AIR, Material.REDSTONE_TORCH};

    /**
     * Finds a suitable 3-blocks tall space for a portal to spawn in.
     * This will find space under the player before on top of the player.
     *
     * @param player The reference player.
     * @return The center location of a 3-block AIR space.
     */
    public static Location findPortalLocationFromPlayer(Player player) {
        Location randomLocationInRange = getRandomLocationInRange(player.getEyeLocation(), Configuration.rangeMinPortal, Configuration.rangeMaxPortal);
        return getThreeBlocksSpaceInColumn(randomLocationInRange);
    }

    /**
     * Get a random location in a mix and max range from the given location.
     * Only cares about X and Z.
     *
     * @param location Central location.
     * @param rangeMin Min range.
     * @param rangeMax Max range.
     * @return The random location selected.
     * @throws IllegalArgumentException if arguments are invalid (min > max).
     */
    @SuppressWarnings("SameParameterValue")
    private static Location getRandomLocationInRange(Location location, int rangeMin, int rangeMax) {
        if (rangeMax > rangeMin) {
            Random random = new Random();
            int randomX = (Math.random() < 0.5 ? 1 : -1) * random.nextInt(rangeMax - rangeMin) - rangeMin / 2;
            int randomZ = (Math.random() < 0.5 ? 1 : -1) * random.nextInt(rangeMax - rangeMin) - rangeMin / 2;
            return location.clone().add(randomX, 0, randomZ);
        } else {
            throw new IllegalArgumentException("rangeMax cannot be smaller than or equal to rangeMin");
        }
    }

    /**
     * Get the first three-blocks vertical space in the selected location's column.
     * Search first towards the bottom.
     *
     * @param location Location of the column.
     * @return The suitable location, or null if none is found.
     */
    private static Location getThreeBlocksSpaceInColumn(Location location) {
        final Location cloned = location.clone().toCenterLocation();
        double startY = location.getY();
        for (double i = startY; i > 1; i -= 1) {
            cloned.setY(i);
            if (checkThreeBlockAirSpace(cloned)) {
                return cloned;
            }
        }
        for (double i = startY; i < 254; i += 1) {
            cloned.setY(i);
            if (checkThreeBlockAirSpace(cloned)) {
                return cloned;
            }
        }
        return null;
    }

    /**
     * Check if there are three blocks of AIR, given a central location.
     *
     * @param location Central location.
     * @return True if there is a 3-blocks AIR space, false otherwise.
     */
    public static boolean checkThreeBlockAirSpace(Location location) {
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
