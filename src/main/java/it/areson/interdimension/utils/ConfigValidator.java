package it.areson.interdimension.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ConfigValidator {

    public static FileConfiguration configuration;

    public static void setFileConfiguration(FileConfiguration fileConfiguration) {
        configuration = fileConfiguration;
    }

    public boolean isProbabilityPresent() {
        return configuration.isDouble("spawn-probability-per-night");
    }

    public boolean isProbabilityValid() {
        return configuration.getDouble("spawn-probability-per-night") >= 0 && configuration.getDouble("spawn-probability-per-night") <= 1;
    }

    public boolean isDestinationPresent() {
        return configuration.isConfigurationSection("destination");
    }

    public boolean isDestinationValid() {
        ConfigurationSection destination = configuration.getConfigurationSection("destination");
        if(Objects.isNull(destination)){
            return false;
        }
        return destination.isString("world") &&
                destination.isDouble("x") &&
                destination.isDouble("y") &&
                destination.isDouble("z") &&
                destination.isDouble("yaw") &&
                destination.isDouble("pitch");
    }

}
