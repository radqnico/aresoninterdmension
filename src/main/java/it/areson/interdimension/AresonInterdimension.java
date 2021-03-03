package it.areson.interdimension;

import it.areson.interdimension.commands.*;
import it.areson.interdimension.events.PlayerEvents;
import it.areson.interdimension.portals.PortalManager;
import it.areson.interdimension.utils.AresonConfiguration;
import it.areson.interdimension.utils.ConfigurationFile;
import it.areson.interdimension.utils.FileManager;
import it.areson.interdimension.utils.MessageManager;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class AresonInterdimension extends JavaPlugin {
    public MessageManager messages;
    public FileManager data;
    public PortalManager portalManager;
    public World portalsWorld;
    public PlayerEvents playerEvents;
    public GeneralTask generalTask;
    public ConfigurationFile fileConfiguration = new ConfigurationFile();

    @Override
    public void onEnable() {
        messages = new MessageManager(this, "messages.yml");
        data = new FileManager(this, "data.yml");
        try {
            fileConfiguration.setFileConfiguration(data.getFileConfiguration());
        } catch (AresonConfiguration.InvalidAresonConfigurationException e) {
            e.printStackTrace();
        }
        init();
        registerCommands();
    }

    public void init() {
        portalsWorld = getServer().getWorld("world");
        playerEvents = new PlayerEvents(this);
        portalManager = new PortalManager(this, playerEvents);
        generalTask = new GeneralTask(this);
        generalTask.startTask();
    }

    public void registerCommands() {
        new SetProbabilityCommand(this);
        new SetDestinationCommand(this);
        new TogglePortalsCommand(this);
        new ReloadInterdimensionCommand(this);
        new SetPortalChestCommand(this);
    }
}
