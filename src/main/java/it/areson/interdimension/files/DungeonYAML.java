package it.areson.interdimension.files;

import it.areson.interdimension.dungeon.Dungeon;
import it.areson.interdimension.dungeon.DungeonManager;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DungeonYAML extends FileManager {

    public DungeonYAML(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public void readDungeons(DungeonManager dungeonManager) {
        if (validateConfig()) {
            plugin.getLogger().info(String.format("Found %d dungeons in file", fileConfiguration.getKeys(false).size()));
            for (String dungeonKey : fileConfiguration.getKeys(false)) {
                plugin.getLogger().info(String.format("Reading dungeon '%s'", dungeonKey));
                ConfigurationSection dungeonSection = fileConfiguration.getConfigurationSection(dungeonKey);
                if (dungeonSection != null) {
                    Dungeon dungeon = new Dungeon(dungeonKey);

                    Optional<Location> location = getLocation(dungeonSection, "location");
                    if (location.isPresent()) {
                        dungeon.setLocation(location.get());

                        ConfigurationSection chestsSection = dungeonSection.getConfigurationSection("chests");
                        if (chestsSection != null) {
                            plugin.getLogger().info(String.format("Found %d chests in dungeon '%s'", chestsSection.getKeys(false).size(), dungeonKey));
                            for (String chestKey : chestsSection.getKeys(false)) {
                                Optional<Location> chestLocation = getLocation(chestsSection, chestKey);
                                chestLocation.ifPresent(dungeon::addChest);
                            }
                        } else {
                            plugin.getLogger().warning(String.format("Dungeon '%s' chests section is null", dungeonKey));
                        }

                        dungeonManager.addDungeon(dungeon);
                    } else {
                        plugin.getLogger().warning(String.format("Dungeon '%s' location is null", dungeonKey));
                    }
                } else {
                    plugin.getLogger().warning(String.format("Dungeon '%s' section is null", dungeonKey));
                }
            }
            plugin.getLogger().info(String.format(ChatColor.YELLOW + "%d dungeons read correctly", dungeonManager.getDungeons().size()));
        } else {
            plugin.getLogger().warning("Config is not valid. Not reading dungeons.");
        }
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
            save();
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
    public List<String> assertDungeonsConfig() {
        List<String> errors = new ArrayList<>();
        for (String key : fileConfiguration.getKeys(false)) {
            if (!fileConfiguration.isConfigurationSection(key)) {
                errors.add(key + " is not a ConfigurationSection");
            }
        }
        return errors;
    }

    @ConfigAssert
    public List<String> assertDungeonsLocations() {
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
    public List<String> assertDungeonsChestConfig() {
        List<String> errors = new ArrayList<>();
        for (String dungeonKey : fileConfiguration.getKeys(false)) {
            ConfigurationSection dungeonSection = fileConfiguration.getConfigurationSection(dungeonKey);
            if (dungeonSection == null) {
                errors.add(dungeonKey + " is null. Maybe there are no chest in dungeon '" + dungeonKey + "'?");
                break;
            }
            String path = dungeonKey + ".chests";
            if (!dungeonSection.isConfigurationSection("chests")) {
                errors.add(path + " is not a ConfigurationSection. Maybe there are no chest in dungeon '" + dungeonKey + "'?");
            }
        }
        return errors;
    }

    @ConfigAssert
    public List<String> assertDungeonsChests() {
        List<String> errors = new ArrayList<>();
        for (String dungeonKey : fileConfiguration.getKeys(false)) {
            ConfigurationSection dungeonSection = fileConfiguration.getConfigurationSection(dungeonKey);
            if (dungeonSection == null) {
                errors.add(dungeonKey + " is null");
                break;
            }
            String chestsSectionPath = dungeonKey + ".chests";
            ConfigurationSection chestSection = dungeonSection.getConfigurationSection("chests");
            if (chestSection == null) {
                errors.add(chestsSectionPath + " is null. Maybe there are no chest in dungeon '" + dungeonKey + "'?");
                break;
            }
            for (String chestKey : chestSection.getKeys(false)) {
                String chestPath = chestsSectionPath + "." + chestKey;
                if (isNotLocationConfig(chestPath)) {
                    errors.add(chestPath + " is not a Location. Maybe there are no chest in dungeon '" + dungeonKey + "'?");
                }
            }
        }
        return errors;
    }

    public boolean validateConfig() {
        boolean isOk = true;
        plugin.getLogger().info(ChatColor.YELLOW + "Validating config...");
        Method[] methods = this.getClass().getMethods();
        List<Method> validators = Arrays.stream(methods).filter(method -> method.isAnnotationPresent(ConfigAssert.class)).collect(Collectors.toList());
        plugin.getLogger().info(ChatColor.YELLOW + "Found " + validators.size() + " validating methods");
        for (Method method : methods) {
            if (method.isAnnotationPresent(ConfigAssert.class)) {
                try {
                    List<String> invoke = (List<String>) method.invoke(this);
                    if (invoke.size() > 0) {
                        for (String error : invoke) {
                            plugin.getLogger().warning("Dungeons config. Something wrong in method " + method.getName() + " with comment: " + error);
                        }
                        isOk = false;
                    } else {
                        plugin.getLogger().info(ChatColor.GREEN + method.getName() + " found no errors");
                    }
                } catch (Exception e) {
                    plugin.getLogger().severe("Exception in dungeons config validation in method " + method.getName());
                    e.printStackTrace();
                }
            }
        }
        return isOk;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ConfigAssert {
    }
}
