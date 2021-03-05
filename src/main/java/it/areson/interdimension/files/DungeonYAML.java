package it.areson.interdimension.files;

import it.areson.interdimension.dungeon.Dungeon;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DungeonYAML extends FileManager {

    public DungeonYAML(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
        validateConfig();
    }

    public void saveDungeon(Dungeon dungeon) {
        if (dungeon != null) {
            String name = dungeon.getName();
            if (fileConfiguration.isConfigurationSection(name)) {
                fileConfiguration.set(name, null);
            }
            ConfigurationSection dungeonSection = fileConfiguration.createSection(name);
            setLocation(dungeonSection, "location", dungeon.getLocation());
            ConfigurationSection chestsSection = dungeonSection.createSection("chests");
            List<Location> chests = dungeon.getChests();
            for (int i = 0; i < chests.size(); i++) {
                setLocation(chestsSection, "" + i, chests.get(i));
            }
            plugin.getLogger().info("Dungeon " + dungeon.getName() + " saved");
            validateConfig();
        } else {
            plugin.getLogger().warning("Dungeon passed to save is null");
        }
    }

    private boolean isNotLocationConfig(String path) {
        if (!fileConfiguration.isConfigurationSection(path)) {
            return true;
        }
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection(path);
        if (configurationSection == null) {
            return true;
        }
        return !configurationSection.isString("world") ||
                !configurationSection.isDouble("x") ||
                !configurationSection.isDouble("y") ||
                !configurationSection.isDouble("z") ||
                !configurationSection.isDouble("yaw") ||
                !configurationSection.isDouble("pitch");
    }

    @ConfigAssert
    private List<String> assertDungeonsConfig() {
        List<String> errors = new ArrayList<>();
        for (String key : fileConfiguration.getKeys(false)) {
            if (!fileConfiguration.isConfigurationSection(key)) {
                errors.add(key + " is not a ConfigurationSection");
            }
        }
        return errors;
    }

    @ConfigAssert
    private List<String> assertDungeonsLocations() {
        List<String> errors = new ArrayList<>();
        for (String key : fileConfiguration.getKeys(false)) {
            ConfigurationSection dungeonSection = fileConfiguration.getConfigurationSection(key);
            if (dungeonSection == null) {
                errors.add(key + " is null");
                break;
            }
            String path = key + ".location";
            if (isNotLocationConfig(path)) {
                errors.add(path + " is not a Location");
            }
        }
        return errors;
    }

    @ConfigAssert
    private List<String> assertDungeonsChestConfig() {
        List<String> errors = new ArrayList<>();
        for (String key : fileConfiguration.getKeys(false)) {
            ConfigurationSection dungeonSection = fileConfiguration.getConfigurationSection(key);
            if (dungeonSection == null) {
                errors.add(key + " is null");
                break;
            }
            String path = key + ".chests";
            if (!dungeonSection.isConfigurationSection(path)) {
                errors.add(path + " is not a ConfigurationSection");
            }
        }
        return errors;
    }

    @ConfigAssert
    private List<String> assertDungeonsChests() {
        List<String> errors = new ArrayList<>();
        for (String dungeonKey : fileConfiguration.getKeys(false)) {
            ConfigurationSection dungeonSection = fileConfiguration.getConfigurationSection(dungeonKey);
            if (dungeonSection == null) {
                errors.add(dungeonKey + " is null");
                break;
            }
            String chestsSectionPath = dungeonKey + ".chests";
            ConfigurationSection chestSection = dungeonSection.getConfigurationSection(chestsSectionPath);
            if (chestSection == null) {
                errors.add(chestsSectionPath + " is null");
                break;
            }
            for (String chestKey : chestSection.getKeys(false)) {
                String chestPath = chestsSectionPath + "." + chestKey;
                if (isNotLocationConfig(chestPath)) {
                    errors.add(chestPath + " is not a Location");
                }
            }
        }
        return errors;
    }

    public void validateConfig() {
        plugin.getLogger().info(ChatColor.YELLOW + "Validating config...");
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ConfigAssert.class)) {
                try {
                    List<String> invokes = (List<String>) method.invoke(null, (Object) null);
                    if (invokes.size() > 0) {
                        for (String error : invokes) {
                            plugin.getLogger().severe("Error in dungeons config. Error in method " + method.getName() + " with comment: " + error);
                        }
                    } else {
                        plugin.getLogger().info(ChatColor.GREEN + "Config is valid.");
                    }
                } catch (Exception e) {
                    plugin.getLogger().severe("Exception in dungeons config validation in method " + method.getName());
                }
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ConfigAssert {
    }
}
