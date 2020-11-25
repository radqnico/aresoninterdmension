package it.areson.interdimension.events;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.portals.Portal;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerPassPortalEvents implements Listener {

    private AresonInterdimension plugin;
    private boolean isRegistered;

    public PlayerPassPortalEvents(AresonInterdimension plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {
        if (!isRegistered) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            isRegistered = true;
        }
    }

    public void unregisterEvents() {
        if (isRegistered) {
            HandlerList.unregisterAll(this);
            isRegistered = false;
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        if (plugin.portalManager.getActivePortal().isPresent()) {
            Portal portal = plugin.portalManager.getActivePortal().get();
            Location toLocation = e.getTo();
            if (toLocation != null) {
                if (toLocation.getChunk().equals(portal.getPortalChunk())) {
                    if (toLocation.distance(portal.getLocation()) < .5 ||
                            e.getPlayer().getEyeLocation().clone().distance(portal.getLocation()) < .5 ||
                            toLocation.distance(portal.getLocation().clone().subtract(0, 1, 0)) < .5 ||
                            toLocation.distance(portal.getLocation().clone().subtract(0, 2, 0)) < .5
                    ) {
                        portal.teleport(e.getPlayer());
                        portal.spark();
                        plugin.portalManager.setPortalPassed(true);
                        plugin.portalManager.startGoBackTask(portal.getLocation().clone(), e.getPlayer());
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false, false));
                        portal.deactivate();
                        plugin.portalManager.removePortal();
                        plugin.getServer().broadcastMessage(
                                ChatColor.translateAlternateColorCodes('&', plugin.messages.getPlainMessage("portal-entered-broadcast")
                                        .replaceAll("%PLAYER%", e.getPlayer().getName()))
                        );
                    }
                }
            }
        }
    }
}
