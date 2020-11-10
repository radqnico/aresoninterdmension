package it.areson.interdimension;

import it.areson.interdimension.commands.SetDestinationCommand;
import it.areson.interdimension.commands.TestPortalCommand;
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
        messagesFile = new FileManager(this, "messages.yml");
        registerCommands();
    }

    public void registerCommands(){
        new TestPortalCommand(this);
        new SetDestinationCommand(this);
    }
}
