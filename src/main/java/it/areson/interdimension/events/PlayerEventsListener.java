package it.areson.interdimension.events;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.portals.Portal;
import it.areson.interdimension.portals.PortalHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;
import java.util.Set;

public class PlayerEventsListener extends GeneralEventListener {

    public PlayerEventsListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Location toLocation = event.getPlayer().getEyeLocation();
        Set<Portal> portals = AresonInterdimension.getInstance().getPortalHandler().getPortals();
        // Check first if any portal is in same chunk as the player that moves.
        boolean anyMatchChunk = portals.stream().anyMatch(portal -> portal.getLocation().getChunk().equals(toLocation.getChunk()));
        if (anyMatchChunk) {
            Optional<Portal> first = portals.stream().filter(portal -> portal.getStatus().equals(Portal.Status.OPEN) && portal.getLocation().distance(toLocation) < 1).findFirst();
            // Check if the player eyes is in the same block of a portal's central block.
            if (first.isPresent()) {
                Portal portal = first.get();
                Player player = event.getPlayer();
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5, 2, false, false, false));
                player.teleport(portal.getDestination().getLocation());
                portal.playerPassedPortal(player);
                AresonInterdimension.sendBroadcastEnterPortalMessage(player);
                // Effetti
                portal.playTeleportEffects();
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Set<Portal> portals = AresonInterdimension.getInstance().getPortalHandler().getPortals();
        for (Portal portal : portals) {
            if (portal.returnBackIfPassed(player)) {
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                break;
            }
        }
    }
}
