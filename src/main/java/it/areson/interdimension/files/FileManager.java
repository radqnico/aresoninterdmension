package it.areson.interdimension.files;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class FileManager {

    private final File file;
    protected JavaPlugin plugin;
    protected FileConfiguration fileConfiguration;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public FileManager(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        file = new File(this.plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            this.plugin.saveResource(fileName, true);

        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBytes(String path, byte[] byteArray) {
        String encoded = Base64.getEncoder().encodeToString(byteArray);
        fileConfiguration.set(path, encoded);
        save();
    }

    public byte[] readBytes(String path) {
        String string = fileConfiguration.getString(path);
        if (string != null) {
            return Base64.getDecoder().decode(string.getBytes());
        }
        return null;
    }

    public Optional<Location> getLocation(String path) {
        String worldName = fileConfiguration.getString(path + ".world");
        if (worldName != null) {
            World world = this.plugin.getServer().getWorld(worldName);
            if (world != null) {
                return Optional.of(new Location(
                        world,
                        fileConfiguration.getDouble(path + ".x"),
                        fileConfiguration.getDouble(path + ".y"),
                        fileConfiguration.getDouble(path + ".z"),
                        (float) fileConfiguration.getDouble(path + ".yaw"),
                        (float) fileConfiguration.getDouble(path + ".pitch")
                ));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public void setLocation(Location location, String path) {
        World world = location.getWorld();
        if (world != null) {
            fileConfiguration.set(path + ".world", world.getName());
            fileConfiguration.set(path + ".x", location.getX());
            fileConfiguration.set(path + ".y", location.getY());
            fileConfiguration.set(path + ".z", location.getZ());
            fileConfiguration.set(path + ".yaw", location.getYaw());
            fileConfiguration.set(path + ".pitch", location.getPitch());
        }
        save();
    }

}

