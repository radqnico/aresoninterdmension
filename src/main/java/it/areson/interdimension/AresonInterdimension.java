package it.areson.interdimension;

import it.areson.interdimension.commands.SetDestinationCommand;
import it.areson.interdimension.commands.SetProbabilityCommand;
import it.areson.interdimension.events.PlayerPassPortalEvents;
import it.areson.interdimension.portals.PortalManager;
import it.areson.interdimension.utils.ConfigValidator;
import it.areson.interdimension.utils.FileManager;
import it.areson.interdimension.utils.GeneralTask;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class AresonInterdimension extends JavaPlugin {

    public static AresonInterdimension instance;
    public FileManager messagesFile;
    public PortalManager portalManager;
    public World portalsWorld;
    public PlayerPassPortalEvents passPortalEvents;
    public GeneralTask generalTask;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigValidator.setFileConfiguration(getConfig());
        init();
        registerCommands();
    }

    public void init() {
        messagesFile = new FileManager(this, "messages.yml");
        if (!validateConfig()) {
            getLogger().warning("Config is not complete. Use commands to se all values, then /interdimreload");
        } else {
            portalsWorld = getServer().getWorld("world");
            portalManager = new PortalManager(this);
            passPortalEvents = new PlayerPassPortalEvents(this);
            generalTask = new GeneralTask(this);
            generalTask.startTask();
        }
    }

    public void registerCommands() {
        new SetProbabilityCommand(this);
        new SetDestinationCommand(this);
    }

    public boolean validateConfig() {
        if (!ConfigValidator.isProbabilityPresent()) {
            getServer().getLogger().warning(ChatColor.translateAlternateColorCodes('&', messagesFile.getConfig().getString("probability-not-set")));
            return false;
        } else {
            if (!ConfigValidator.isProbabilityValid()) {
                getServer().getLogger().warning(ChatColor.translateAlternateColorCodes('&', messagesFile.getConfig().getString("probability-not-valid")));
                return false;
            }
        }
        if (!ConfigValidator.isDestinationPresent()) {
            getServer().getLogger().warning(ChatColor.translateAlternateColorCodes('&', messagesFile.getConfig().getString("destination-not-set")));
            return false;
        } else {
            if (!ConfigValidator.isDestinationValid()) {
                getServer().getLogger().warning(ChatColor.translateAlternateColorCodes('&', messagesFile.getConfig().getString("destination-not-valid")));
                return false;
            }
        }
        return true;
    }
}
