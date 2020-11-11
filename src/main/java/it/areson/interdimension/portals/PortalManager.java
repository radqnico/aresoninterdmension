package it.areson.interdimension.portals;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Optional;

public class PortalManager {

    private Optional<Portal> activePortal;
    private AresonInterdimension plugin;
    private Location destination;
    private double probability;

    public PortalManager(AresonInterdimension plugin) {
        this.activePortal = Optional.empty();
        this.plugin = plugin;
        ConfigurationSection destinationConfig = plugin.getConfig().getConfigurationSection("destination");
        assert destinationConfig!=null;
        String world = destinationConfig.getString("world");
        assert world!=null;
        destination = new Location(
                plugin.getServer().getWorld(world),
                destinationConfig.getDouble("x"),
                destinationConfig.getDouble("y"),
                destinationConfig.getDouble("z"),
                (float)destinationConfig.getDouble("yaw"),
                (float)destinationConfig.getDouble("pitch")
        );
        probability = plugin.getConfig().getDouble("spawn-probability-every-five-seconds");
        assert probability>0 && probability <= 1;
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

    public Location getDestination() {
        return destination;
    }

    public double getProbability() {
        return probability;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
