package it.areson.interdimension.files;

import it.areson.interdimension.Configuration;
import org.bukkit.plugin.java.JavaPlugin;


public class DataFile extends FileManager {

    public DataFile(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public double getPortalProbability() {
        return fileConfiguration.getDouble("spawn-probability-every-five-seconds", Configuration.defaultProbabilityToSpawnEveryFiveSeconds);
    }

    public void setPortalProbability(double probability) {
        fileConfiguration.set("spawn-probability-every-five-seconds", probability);
    }

}
