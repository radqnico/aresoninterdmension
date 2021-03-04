package it.areson.interdimension;

import it.areson.interdimension.commands.TestPortalCommand;
import it.areson.interdimension.files.MessageManager;
import it.areson.interdimension.portals.PortalHandler;
import it.areson.interdimension.runnables.PortalCountdown;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AresonInterdimension extends JavaPlugin {

    private static AresonInterdimension instance;
    private MessageManager messages;
    private PortalHandler portalHandler;

    public static AresonInterdimension getInstance() {
        return instance;
    }

    public static void sendBroadcastEnterPortalMessage(Player player) {
        instance.getServer().broadcastMessage(String.format("Il giocatore %s ha attraversato un portale!", player.getName()));
    }

    @Override
    public void onEnable() {
        instance = this;
        messages = new MessageManager(this, "messages.yml");
        portalHandler = new PortalHandler(this);
        PortalCountdown.registerListener(portalHandler);
        portalHandler.addDestination(new Location(getServer().getWorld("world"), 0, 70, 0));
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
