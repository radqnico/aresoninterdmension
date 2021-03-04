package it.areson.interdimension.dungeon;

import java.util.ArrayList;
import java.util.List;

public class DungeonManager {
    private List<Dungeon> dungeons = new ArrayList<>();

    public Dungeon getDungeon(String name) {
        for (Dungeon d : this.dungeons) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }

    public boolean removeDungeon(String name) {
        Dungeon[] filteredDungeons = (Dungeon[]) this.dungeons.stream().filter(d -> d.getName().equals(name)).toArray();
        if (filteredDungeons.length != 1) {
            return false;
        }
        this.dungeons.remove(filteredDungeons[0]);
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

    public List<Dungeon> getDungeons() {
        return this.dungeons;
    }
}
