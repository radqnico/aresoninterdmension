package it.areson.interdimension.dungeon;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    private Location location;
    private final List<Location> chests = new ArrayList<>();
    private final String name;

    public Dungeon (String name) {
        this.name = name;
    }

    public void clearChests() {
        this.chests.clear();
    }

    public void addChest(Location location) {
        this.chests.add(location);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public List<Location> getChests() {
        return chests;
    }
}
