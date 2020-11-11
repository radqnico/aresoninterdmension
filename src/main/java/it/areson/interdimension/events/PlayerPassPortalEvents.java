package it.areson.interdimension.events;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.portals.Portal;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerPassPortalEvents implements Listener {

    private AresonInterdimension plugin;
    private boolean isRegistered;

    public PlayerPassPortalEvents(AresonInterdimension plugin) {
        this.plugin = plugin;
    }

    public void registerThisEvents(){
        if(!isRegistered){
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            isRegistered = true;
        }
    }

    public void unregisterThisEvents(){
        if(isRegistered){
            HandlerList.unregisterAll(this);
            isRegistered = false;
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){
        if (plugin.portalManager.getActivePortal().isPresent()) {
            Portal portal = plugin.portalManager.getActivePortal().get();
            Location toLocation = e.getTo();
            if(toLocation!=null) {
                if (toLocation.getChunk().equals(portal.getPortalChunk())) {
                    Location to = toLocation;
                    if (to.distance(portal.getLocation()) < 0.5) {
                        portal.teleport(e.getPlayer());
                        portal.spark();
                        portal.deactivate();
                        plugin.portalManager.removePortal();
                    }
                }
            }
        }
    }
}
