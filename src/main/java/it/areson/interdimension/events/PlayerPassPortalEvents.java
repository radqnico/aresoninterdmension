package it.areson.interdimension.events;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.portals.Portal;
import it.areson.interdimension.portals.PortalManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.PluginEvent;
import org.bukkit.plugin.PluginManager;

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
        if (plugin.portalmanager.getActivePortal().isPresent()) {
            Portal portal = plugin.portalmanager.getActivePortal().get();
            if (e.getTo().getChunk().equals(portal.getPortalChunk())) {
                Location to = e.getTo();
                if(to.distance(portal.getLocation())<0.5){
                    portal.teleport(e.getPlayer());
                    portal.spark();
                    portal.deactivate();
                    plugin.portalmanager.removePortal();
                }
            }
        }
    }
}
