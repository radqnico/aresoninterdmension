package it.areson.interdimension;

import it.areson.interdimension.commands.TestPortalCommand;
import it.areson.interdimension.files.MessageManager;
import it.areson.interdimension.portals.PortalHandler;
import it.areson.interdimension.runnables.PortalCountdown;
import org.bukkit.plugin.java.JavaPlugin;

public class AresonInterdimension extends JavaPlugin {

    private static AresonInterdimension instance;
    private MessageManager messages;
    private PortalHandler portalHandler;

    public static AresonInterdimension getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        messages = new MessageManager(this, "messages.yml");
        portalHandler = new PortalHandler(this);
        PortalCountdown.registerListener(portalHandler);

        // Test command
        getCommand("testportal").setExecutor(new TestPortalCommand());
    }

    public PortalHandler getPortalHandler() {
        return portalHandler;
    }

    public MessageManager messages() {
        return messages;
    }
}
