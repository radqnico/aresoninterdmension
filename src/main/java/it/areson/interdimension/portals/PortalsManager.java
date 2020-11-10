package it.areson.interdimension.portals;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class PortalsManager {

    private ArrayList<Portal> portals;
    private JavaPlugin plugin;

    public PortalsManager(JavaPlugin plugin) {
        this.portals = new ArrayList<>();
        this.plugin = plugin;
    }

    public synchronized int createNewPortal(Location location, Location destination, int secondsTimeout) {
        Portal portal = new Portal(plugin, location, destination, secondsTimeout);
        portals.add(portal);
        portal.activate();
        return portals.size()-1;
    }

    public void removePortal(Location location) {

    }

}
