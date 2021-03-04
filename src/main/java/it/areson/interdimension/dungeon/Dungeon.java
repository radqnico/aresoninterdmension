package it.areson.interdimension.dungeon;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    private final List<Location> chests = new ArrayList<>();
    private final String name;
    private Location location;
    private boolean alreadyActive;

    public Dungeon(String name) {
        this.name = name;
        this.alreadyActive = false;
    }

    public boolean isAlreadyActive() {
        return alreadyActive;
    }

    public void setAlreadyActive(boolean alreadyActive) {
        this.alreadyActive = alreadyActive;
    }

    public void clearChests() {
        this.chests.clear();
    }

    public void addChest(Location location) {
        this.chests.add(location);
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void resetChestsContent() {
        for (Location location : chests) {
            ChestContent.emptyChest(location);
        }
    }

    /**
     * Return a list of locations of the chests in the dungeon
     *
     * @return List of locations
     */
    public List<Location> getChests() {
        return chests;
    }
}
