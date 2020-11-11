package it.areson.interdimension.portals;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Optional;

public class PortalManager {

    private Optional<Portal> activePortal;
    private AresonInterdimension plugin;

    public PortalManager(AresonInterdimension plugin) {
        this.activePortal = Optional.empty();
        this.plugin = plugin;
    }

    public boolean createNewPortal(Location location, Location destination, int secondsTimeout) {
        if(activePortal.isPresent()){
            return false;
        }
        Portal portal = new Portal(plugin, location, destination, secondsTimeout);
        activePortal = Optional.of(portal);
        activePortal.get().activate();
        plugin.passPortalEvents.registerThisEvents();
        return true;
    }

    public void removePortal() {
        activePortal = Optional.empty();
        plugin.passPortalEvents.unregisterThisEvents();
    }

    public Optional<Portal> getActivePortal() {
        return activePortal;
    }

}
