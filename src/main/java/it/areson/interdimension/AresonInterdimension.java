package it.areson.interdimension;

import it.areson.interdimension.commands.*;
import it.areson.interdimension.events.PlayerEvents;
import it.areson.interdimension.portals.PortalManager;
import it.areson.interdimension.utils.ConfigValidator;
import it.areson.interdimension.utils.FileManager;
import it.areson.interdimension.utils.MessageManager;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class AresonInterdimension extends JavaPlugin {
    //TODO Useless commit

    public MessageManager messages;
    public FileManager data;
    public PortalManager portalManager;
    public World portalsWorld;
    public PlayerEvents playerEvents;
    public GeneralTask generalTask;

    @Override
    public void onEnable() {
        messages = new MessageManager(this, "messages.yml");
        data = new FileManager(this, "data.yml");
        ConfigValidator.setFileConfiguration(data.getFileConfiguration());
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

    public boolean validateConfig() {
        if (!ConfigValidator.isProbabilityPresent()) {
            getServer().getLogger().warning(messages.getPlainMessage("probability-not-set"));
            return false;
        } else {
            if (!ConfigValidator.isProbabilityValid()) {
                getServer().getLogger().warning(messages.getPlainMessage("probability-not-valid"));
                return false;
            }
        }
        if (!ConfigValidator.isDestinationPresent()) {
            getServer().getLogger().warning(messages.getPlainMessage("destination-not-set"));
            return false;
        } else {
            if (!ConfigValidator.isDestinationValid()) {
                getServer().getLogger().warning(messages.getPlainMessage("destination-not-valid"));
                return false;
            }
        }
        return true;
    }
}
