package it.areson.interdimension;

import it.areson.interdimension.portals.PortalsManager;
import it.areson.interdimension.utils.FileManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AresonInterdimension extends JavaPlugin {

    public static AresonInterdimension instance;
    public FileManager messagesFile;
    public PortalsManager portalsManager;

    @Override
    public void onEnable() {
        instance = this;
        portalsManager = new PortalsManager(this);
    }
}
