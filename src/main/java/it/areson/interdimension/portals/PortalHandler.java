package it.areson.interdimension.portals;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.Configuration;
import it.areson.interdimension.dungeon.Dungeon;
import it.areson.interdimension.dungeon.DungeonManager;
import it.areson.interdimension.events.PlayerEventsListener;
import it.areson.interdimension.locationfinder.LocationFinder;
import it.areson.interdimension.runnables.PortalCountdown;
import it.areson.interdimension.runnables.PortalCountdownEndListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
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
     * Player move event listener. Registered only when needed.
     */
    private final PlayerEventsListener playerEventsListener;

    /**
     * Create a new PortalHandler.
     *
     * @param plugin Plugin instance.
     */
    public PortalHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.portals = new HashMap<>();
        playerEventsListener = new PlayerEventsListener(plugin);
    }

    /**
     * Checks if the given player is already into a portal.
     *
     * @param player Player to check
     * @return True if the player is inside a portal, false otherwise.
     */
    public boolean canPortalSpawnAtPlayer(Player player) {
        return getPortals().parallelStream().noneMatch(
                portal -> portal.getWhoPassed().parallelStream().anyMatch(
                        passedPlayer -> passedPlayer.getName().equals(player.getName())
                )
        );
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
                // Everything exists, create portal if player can spawn portals
                if (canPortalSpawnAtPlayer(player)) {
                    // Register events if first
                    if (portals.size() == 0) {
                        playerEventsListener.registerEvents();
                        plugin.getLogger().info("Registered move event");
                    }
                    Portal portal = new Portal(plugin, portalLocationFromPlayer, randomDestination);
                    portals.put(portal, new PortalCountdown(plugin, Configuration.portalDurationSeconds, portal));
                    plugin.getLogger().info(String.format("PORTAL SPAWNING: Portal spawned to player %s", player.getName()));
                    // Open and start counting
                    portal.openPortal();
                    startPortalCountdown(portal);
                    return true;
                } else {
                    plugin.getLogger().warning(String.format("PORTAL SPAWNING: Player %s is already into a portal", player.getName()));
                }
            } else {
                plugin.getLogger().warning(String.format("PORTAL SPAWNING: Could not find suitable portal location for player %s", player.getName()));
            }
        } else {
            plugin.getLogger().warning("PORTAL SPAWNING: No destination has been set.");
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
     * Get a random destination with uniform probability.
     *
     * @return Location of the destination.
     */
    public Location getRandomDestination() {
        DungeonManager dungeonManager = AresonInterdimension.getInstance().getDungeonManager();
        Dungeon dungeon = dungeonManager.randomizeDungeon();
        if (dungeon != null) {
            return dungeon.getLocation();
        }
        return null;
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
        } else {
            plugin.getLogger().severe("No portal countdown found for a portal");
        }
    }

    /**
     * Logic of countdown finish.
     *
     * @param portal Portal attached to the countdown.
     * @see PortalCountdownEndListener
     */
    @Override
    public synchronized void notifyCountdownFinish(Portal portal) {
        portal.closePortal();
        portal.returnBackIfPassed();
        portal.playTeleportEffects();
        portals.remove(portal);
        // Unregister if last
        if (portals.size() == 0) {
            playerEventsListener.unregisterEvents();
            plugin.getLogger().info("Unregistered move event");
        }
    }
}
