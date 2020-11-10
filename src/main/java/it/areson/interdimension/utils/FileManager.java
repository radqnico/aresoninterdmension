/*
 * Copyright (c) 2020. Areson Team (Marco Paggioro, Nicolò Scialpi) all rights reserved.
 */

package it.areson.interdimension.utils;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class made to manage a file in the format YAML
 *
 * @author Areson
 */
public class FileManager {

    protected JavaPlugin plugin;
    protected FileConfiguration config;
    private File file;

    /**
     * Constructor
     *
     * @param plugin   Instance of the plugin
     * @param fileName Name of the file
     */
    public FileManager(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(this.plugin.getDataFolder(), fileName);
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            this.plugin.saveResource(fileName, true);

        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Round a number to 3 decimal digits
     *
     * @param numero Number to round
     * @return Rounded number
     */
    private double round(double numero) {
        return Math.round(numero * 100.0) / 100.0;
    }

    /**
     * Get the configuration of the file
     *
     * @return FileConfiguration object
     */
    public FileConfiguration getConfig() {
        return config;
    }//getConfig


    /**
     * Save the modified file configuration
     */
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//save

    /**
     * Get a Location object from the file
     *
     * @param path Path where the location is stored
     * @return The location object
     */
    public Location getLocation(String path) {
        if (config.getString(path + ".world") == null)
            return null;
        else {
            String world = config.getString(path + ".world");
            if (world != null)
                return new Location(plugin.getServer().getWorld(world), config.getDouble(path + ".x"), config.getDouble(path + ".y"), config.getDouble(path + ".z"), (float) config.getDouble(path + ".yaw"), (float) config.getDouble(path + ".pitch"));
            else
                return null;
        }
    }

    /**
     * Adds to the configuration a Location with a name
     *
     * @param location Location object to save
     * @param path     Location path
     */
    public void setLocation(String path, Location location) {
        config.set(path + ".world", Objects.requireNonNull(location.getWorld()).getName());
        config.set(path + ".x", round(location.getX()));
        config.set(path + ".y", round(location.getY()));
        config.set(path + ".z", round(location.getZ()));
        config.set(path + ".yaw", round(location.getYaw()));
        config.set(path + ".pitch", round(location.getPitch()));
        save();
    }
}
