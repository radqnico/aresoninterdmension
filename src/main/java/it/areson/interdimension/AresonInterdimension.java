package it.areson.interdimension;

import it.areson.interdimension.commands.ReloadInterdimensionCommand;
import it.areson.interdimension.commands.SetDestinationCommand;
import it.areson.interdimension.commands.SetProbabilityCommand;
import it.areson.interdimension.commands.TogglePortalsCommand;
import it.areson.interdimension.events.PlayerPassPortalEvents;
import it.areson.interdimension.portals.PortalManager;
import it.areson.interdimension.utils.ConfigValidator;
import it.areson.interdimension.utils.FileManager;
import it.areson.interdimension.utils.GeneralTask;
import it.areson.interdimension.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class AresonInterdimension extends JavaPlugin {

    public MessageManager messages;
    public FileManager data;
    public PortalManager portalManager;
    public World portalsWorld;
    public PlayerPassPortalEvents playerPassPortalEvents;
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
        playerPassPortalEvents = new PlayerPassPortalEvents(this);
        portalManager = new PortalManager(this, playerPassPortalEvents);
        generalTask = new GeneralTask(this);
        generalTask.startTask();
    }

    public void registerCommands() {
        new SetProbabilityCommand(this);
        new SetDestinationCommand(this);
        new TogglePortalsCommand(this);
        new ReloadInterdimensionCommand(this);
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
