package it.areson.interdimension.events;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.portals.Portal;
import it.areson.interdimension.portals.PortalHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;
import java.util.Set;

public class PlayerMoveListener extends GeneralEventListener {

    public PlayerMoveListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Location toLocation = event.getPlayer().getEyeLocation();
        AresonInterdimension instance = AresonInterdimension.getInstance();
        PortalHandler portalHandler = instance.getPortalHandler();
        Set<Portal> portals = portalHandler.getPortals();
        // Check first if any portal is in same chunk as the player that moves.
        boolean anyMatchChunk = portals.parallelStream().anyMatch(portal -> portal.getLocation().getChunk().equals(toLocation.getChunk()));
        if (anyMatchChunk) {
            Optional<Portal> first = portals.parallelStream().filter(portal -> portal.getStatus().equals(Portal.Status.OPEN) && portal.getLocation().distance(toLocation) < 1).findFirst();
            // Check if the player eyes is in the same block of a portal's central block.
            if (first.isPresent()) {
                Portal portal = first.get();
                Player player = event.getPlayer();
                player.teleportAsync(portal.getDestination()).whenComplete((result, ignored) -> {
                    if (result) {
                        portal.playerPassedPortal(player);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2, 1, false, false, false));
                        // Broadcast
                        // Suoni
                    } else {
                        plugin.getLogger().warning(String.format("Could not teleport player %s", player.getName()));
                    }
                });
            }
        }
    }
}
