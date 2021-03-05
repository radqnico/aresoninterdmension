package it.areson.interdimension.dungeon;

import it.areson.interdimension.files.DungeonYAML;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DungeonManager {
    private final List<Dungeon> dungeons = new ArrayList<>();

    /**
     * Get a dungeon by name.
     *
     * @param name Name of the dungeon
     * @return Dungeon or null if a dungeon with the given name doesn't exist
     */
    public Dungeon getDungeon(String name) {
        for (Dungeon d : this.dungeons) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }

    public boolean removeDungeon(String name) {
        List<Dungeon> filteredDungeons = this.dungeons.stream().filter(d -> d.getName().equals(name)).collect(Collectors.toList());
        if (filteredDungeons.size() != 1) {
            return false;
        }
        this.dungeons.remove(filteredDungeons.get(0));
        return true;
    }

    public List<String> getDungeonNames() {
        List<String> names = new ArrayList<>();
        for (Dungeon dungeon : this.dungeons) {
            names.add(dungeon.getName());
        }
        return names;
    }

    public boolean addDungeon(Dungeon dungeon) {
        for (Dungeon d : this.dungeons) {
            if (d.getName().equals(dungeon.getName())) {
                return false;
            }
        }
        this.dungeons.add(dungeon);
        return true;
    }

    /**
     * Pick a random dungeon from the list
     *
     * @return The randomized dungeon, null if there is no dungeon.
     */
    public Dungeon randomizeDungeon() {
        List<Dungeon> filtered = dungeons.parallelStream().filter(dungeon -> !dungeon.isAlreadyActive()).collect(Collectors.toList());
        if (filtered.size() <= 0) {
            return null;
        }
        Random rand = new Random();
        return filtered.get(rand.nextInt(this.dungeons.size()));
    }

    /**
     * Gets the list of all dungeons
     *
     * @return List of the dungeons
     */
    public List<Dungeon> getDungeons() {
        return this.dungeons;
    }

    public void saveAllDungeonsToFile(DungeonYAML dungeonYAML) {
        for (Dungeon dungeon : dungeons) {
            dungeonYAML.saveDungeon(dungeon);
        }
    }
}
