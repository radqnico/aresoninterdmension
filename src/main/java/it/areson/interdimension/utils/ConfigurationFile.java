package it.areson.interdimension.utils;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class ConfigurationFile extends AresonConfiguration {
    private static final String SpawnProbabilityEveryFiveSeconds = "spawn-probability-every-five-seconds";
    private static final String Destination = "destination";

    public double getProbability() {
        return configuration.getDouble(SpawnProbabilityEveryFiveSeconds);
    }

    public Location getDestination() {
        return configuration.getLocation(Destination);
    }

    @ConfigurationAssert
    public boolean isProbabilityPresent() {
        return configuration.isDouble(SpawnProbabilityEveryFiveSeconds);
    }

    @ConfigurationAssert
    public boolean isProbabilityValid() {
        return configuration.getDouble(SpawnProbabilityEveryFiveSeconds) > 0 && configuration.getDouble(SpawnProbabilityEveryFiveSeconds) < 1;
    }

    @ConfigurationAssert
    public boolean isDestinationPresent() {
        return configuration.isConfigurationSection(Destination);
    }

    @ConfigurationAssert
    public boolean isDestinationValid() {
        ConfigurationSection destination = configuration.getConfigurationSection(Destination);
        if (Objects.isNull(destination)){
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
