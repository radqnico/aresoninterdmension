package it.areson.interdimension.portals;

import it.areson.interdimension.Configuration;
import it.areson.interdimension.events.PlayerMoveListener;
import it.areson.interdimension.locationfinder.LocationFinder;
import it.areson.interdimension.runnables.PortalCountdown;
import it.areson.interdimension.runnables.PortalCountdownEndListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * Portal handler.
 * Spawn new portals and receive countdown notifications.
 */
public class PortalHandler implements PortalCountdownEndListener {

    /**
     * Active portals with countdowns attached.
     * Note: active doesn't mean OPEN.
     */
    private final HashMap<Portal, PortalCountdown> portals;
    /**
     * Plugin instance.
     */
    private final JavaPlugin plugin;
    /**
     * List of possible destinations.
     */
    private final ArrayList<Location> destinations;
    /**
     * Player move event listener. Registered only when needed.
     */
    private final PlayerMoveListener playerMoveListener;

    /**
     * Create a new PortalHandler.
     *
     * @param plugin Plugin instance.
     */
    public PortalHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.portals = new HashMap<>();
        destinations = new ArrayList<>();
        playerMoveListener = new PlayerMoveListener(plugin);
    }

    /**
     * Attempts to spawn a portal near a player.
     *
     * @param player Player to spawn the portal at.
     * @return True if the portal spawned, false otherwise.
     */
    public boolean spawnPortalNearPlayer(Player player) {
        Location randomDestination = getRandomDestination();
        if (randomDestination != null) {
            Location portalLocationFromPlayer = LocationFinder.findPortalLocationFromPlayer(player);
            if (portalLocationFromPlayer != null) {
                // Everything exists, create portal
                // Register events if first
                if (portals.size() == 0) {
                    playerMoveListener.registerEvents();
                }
                Portal portal = new Portal(plugin, portalLocationFromPlayer, randomDestination);
                System.out.println(portalLocationFromPlayer.toString());
                System.out.println(randomDestination.toString());
                portals.put(portal, new PortalCountdown(plugin, Configuration.portalDurationSeconds, portal));
                plugin.getLogger().fine(String.format("Portal spawned to player %s", player.getName()));
                // Open and start counting
                portal.openPortal();
                startPortalCountdown(portal);
                return true;
            } else {
                plugin.getLogger().warning(String.format("Could not find suitable portal location for player %s", player.getName()));
            }
        } else {
            plugin.getLogger().warning("No destination has been set.");
        }
        return false;
    }

    /**
     * Get a set of active portals (both OPEN and CLOSED).
     *
     * @return Set of active portals.
     */
    public Set<Portal> getPortals() {
        return portals.keySet();
    }

    /**
     * Add a possible destination.
     *
     * @param destination The new possible destination.
     */
    public void addDestination(Location destination) {
        destinations.add(destination);
    }

    /**
     * Get a random destination with uniform probability.
     *
     * @return Location of the destination.
     */
    public Location getRandomDestination() {
        if (destinations.size() == 0) {
            return null;
        }
        return destinations.get(new Random().nextInt(destinations.size()));
    }

    /**
     * Clears the list of possible destination.
     */
    public void clearDestinations() {
        destinations.clear();
    }

    /**
     * Starts a specific portal's countdown.
     *
     * @param portal Selected portal to start the countdown.
     */
    public void startPortalCountdown(Portal portal) {
        PortalCountdown portalCountdown = portals.get(portal);
        if (portalCountdown != null) {
            portalCountdown.startCountdown();
        }
    }

    /**
     * Logic of countdown finish.
     *
     * @param portal Portal attached to the countdown.
     * @see PortalCountdownEndListener
     */
    @Override
    public void notifyCountdownFinish(Portal portal) {
        portal.closePortal();
        portal.returnBackWhoPassed();
        portals.remove(portal);
        // Unregister if last
        if (portals.size() == 0) {
            playerMoveListener.unregisterEvents();
        }
        System.out.println("Portal closed.");
    }
}
