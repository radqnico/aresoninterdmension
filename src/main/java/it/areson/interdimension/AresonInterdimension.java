package it.areson.interdimension;

import it.areson.interdimension.commands.SetDestinationCommand;
import it.areson.interdimension.commands.TestPortalCommand;
import it.areson.interdimension.events.PlayerPassPortalEvents;
import it.areson.interdimension.portals.PortalManager;
import it.areson.interdimension.utils.FileManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AresonInterdimension extends JavaPlugin {

    public static AresonInterdimension instance;
    public FileManager messagesFile;
    public PortalManager portalmanager;

    public PlayerPassPortalEvents passPortalEvents;

    @Override
    public void onEnable() {
        instance = this;
        portalmanager = new PortalManager(this);
        messagesFile = new FileManager(this, "messages.yml");

        passPortalEvents = new PlayerPassPortalEvents(this);
        registerCommands();
    }

    public void registerCommands(){
        new TestPortalCommand(this);
        new SetDestinationCommand(this);
    }
}
