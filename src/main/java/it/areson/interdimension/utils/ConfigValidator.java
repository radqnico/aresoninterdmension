package it.areson.interdimension.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ConfigValidator {

    public static FileConfiguration configuration;

    public static void setFileConfiguration(FileConfiguration fileConfiguration) {
        configuration = fileConfiguration;
    }

    public static boolean isProbabilityPresent() {
        return configuration.isDouble("spawn-probability-every-five-seconds");
    }

    public static boolean isProbabilityValid() {
        return configuration.getDouble("spawn-probability-every-five-seconds") > 0 && configuration.getDouble("spawn-probability-every-five-seconds") <= 1;
    }

    public static boolean isDestinationPresent() {
        return configuration.isConfigurationSection("destination");
    }

    public static boolean isDestinationValid() {
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
