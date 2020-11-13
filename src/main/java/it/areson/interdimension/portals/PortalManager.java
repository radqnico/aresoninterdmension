package it.areson.interdimension.portals;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.events.PlayerPassPortalEvents;
import org.bukkit.Location;

import java.util.Objects;
import java.util.Optional;

public class PortalManager {

    private Portal activePortal;
    private AresonInterdimension plugin;
    private PlayerPassPortalEvents playerPassPortalEvents;

    public PortalManager(AresonInterdimension plugin, PlayerPassPortalEvents playerPassPortalEvents) {
        this.playerPassPortalEvents = playerPassPortalEvents;
        this.activePortal = null;
        this.plugin = plugin;
    }

    public boolean createNewPortal(Location location, Location destination, int secondsTimeout) {
        if (Objects.isNull(activePortal)) {
            activePortal = new Portal(plugin, location, destination, secondsTimeout);
            activePortal.activate();
            playerPassPortalEvents.registerEvents();
            return true;
        }
        return false;
    }

    public void removePortal() {
        if (Objects.nonNull(activePortal)) {
            if(activePortal.isActive()) {
                activePortal.deactivate();
            }
            activePortal = null;
            playerPassPortalEvents.unregisterEvents();
        }
    }

    public Optional<Portal> getActivePortal() {
        return Objects.nonNull(activePortal) ? Optional.of(activePortal) : Optional.empty();
    }
}
