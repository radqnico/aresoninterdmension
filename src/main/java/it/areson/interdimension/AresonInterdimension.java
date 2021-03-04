package it.areson.interdimension;

import it.areson.interdimension.commands.*;
import it.areson.interdimension.commands.add.AddChestCommand;
import it.areson.interdimension.commands.add.AddDungeonCommand;
import it.areson.interdimension.commands.rm.RmChestsCommand;
import it.areson.interdimension.commands.rm.RmDungeonCommand;
import it.areson.interdimension.dungeon.DungeonManager;
import it.areson.interdimension.files.MessageManager;
import it.areson.interdimension.portals.PortalHandler;
import it.areson.interdimension.runnables.PortalCountdown;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class AresonInterdimension extends JavaPlugin {
    private static AresonInterdimension instance;
    private final DungeonManager dungeonManager = new DungeonManager();
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
        this.loadCommands();
    }

    public PortalHandler getPortalHandler() {
        return portalHandler;
    }

    public MessageManager messages() {
        return messages;
    }

    public void loadCommands() {
        CommandParser parser = new CommandParser(this);
        PluginCommand command = this.getCommand("interdimension");
        if (command == null) {
            this.getLogger().log(Level.SEVERE, "Cannot register interdimension commands");
            return;
        }
        CommandParser addCommands = new CommandParser(this);
        parser.addCommand("add", addCommands);
        addCommands.addCommand("dungeon", new AddDungeonCommand(this.dungeonManager));
        addCommands.addCommand("chest", new AddChestCommand(this.dungeonManager));
        CommandParser rmCommands = new CommandParser(this);
        parser.addCommand("rm", rmCommands);
        rmCommands.addCommand("dungeon", new RmDungeonCommand(this.dungeonManager));
        rmCommands.addCommand("chests", new RmChestsCommand(this.dungeonManager));
        parser.addCommand("setProbability", new SetProbabilityCommand());
        parser.addCommand("ls", new LsDungeonCommand(this.dungeonManager));
        parser.addCommand("tp", new TpDungeonCommand(this.dungeonManager));
        parser.addCommand("spawnPortal", new TestPortalCommand());
        command.setExecutor(parser);
        command.setTabCompleter(parser);
    }
}
