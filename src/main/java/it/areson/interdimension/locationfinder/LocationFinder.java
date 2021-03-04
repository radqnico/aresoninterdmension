package it.areson.interdimension.locationfinder;

import it.areson.interdimension.Configuration;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Random;

import static org.bukkit.Material.AIR;

/**
 * Tool class with static methods that find a suitable location for a portal to spawn at.
 */
public class LocationFinder {

    /**
     * Finds a suitable 3-blocks tall space for a portal to spawn in.
     * This will find space under the player before on top of the player.
     *
     * @param player The reference player.
     * @return The center location of a 3-block AIR space.
     */
    public static Location findPortalLocationFromPlayer(Player player) {
        Location randomLocationInRange = getRandomLocationInRange(player.getLocation(), Configuration.rangeMinPortal, Configuration.rangeMaxPortal);
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
            int randomX = random.nextInt(rangeMax - rangeMin) + rangeMin;
            int randomZ = random.nextInt(rangeMax - rangeMin) + rangeMin;
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
        final Location cloned = location.clone().toBlockLocation();
        for (int i = (int) location.getY(); i > 1; i--) {
            cloned.setY(i);
            if (checkThreeBlockAirSpace(cloned)) {
                return cloned;
            }
        }
        for (int i = (int) location.getY(); i < 254; i++) {
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
    private static boolean checkThreeBlockAirSpace(Location location) {
        Location cloned = location.clone();
        Block blockCenter = cloned.getBlock();
        Block blockBottom = cloned.clone().add(0, -1, 0).getBlock();
        Block blockTop = cloned.clone().add(0, 1, 0).getBlock();
        return blockBottom.getType().equals(AIR) && blockCenter.getType().equals(AIR) && blockTop.getType().equals(AIR);
    }

}
