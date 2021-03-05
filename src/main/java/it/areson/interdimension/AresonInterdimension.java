package it.areson.interdimension;

import it.areson.interdimension.commands.*;
import it.areson.interdimension.commands.add.AddChestCommand;
import it.areson.interdimension.commands.add.AddDungeonCommand;
import it.areson.interdimension.commands.rm.RmChestsCommand;
import it.areson.interdimension.commands.rm.RmDungeonCommand;
import it.areson.interdimension.dungeon.DungeonManager;
import it.areson.interdimension.files.DataFile;
import it.areson.interdimension.files.DungeonYAML;
import it.areson.interdimension.files.MessageManager;
import it.areson.interdimension.portals.PortalHandler;
import it.areson.interdimension.runnables.PortalCountdown;
import it.areson.interdimension.runnables.RepeatingRunnable;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public class AresonInterdimension extends JavaPlugin {

    private static AresonInterdimension instance;
    private DungeonManager dungeonManager;
    private MessageManager messages;
    private DungeonYAML dungeonsFile;
    private PortalHandler portalHandler;
    private RepeatingRunnable spawnTask;
    private DataFile dataFile;

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
        dataFile = new DataFile(this, "data.yml");
        portalHandler = new PortalHandler(this);
        dungeonManager = new DungeonManager();
        PortalCountdown.registerListener(portalHandler);
        this.loadCommands();
        dungeonsFile = new DungeonYAML(this, "dungeons.yml");
        dungeonsFile.readDungeons(dungeonManager);
        initSpawnTask();
        spawnTask.runRepeatingTask();
    }

    @Override
    public void onDisable() {
        dungeonManager.saveAllDungeonsToFile(dungeonsFile);
    }

    public void initSpawnTask() {
        spawnTask = new RepeatingRunnable(this, 0, 100) {
            @Override
            public void run() {
                // Should spawn?
                boolean shouldSpawn = Math.random() <= dataFile.getPortalProbability();
                if (shouldSpawn) {
                    World world = getServer().getWorld(Configuration.mainWorldName);
                    if (world != null && world.getTime() > 12300 && world.getTime() < 23850) {
                        List<Player> players = world.getPlayers();
                        Player selectedPlayer = players.get((int) (Math.random() * players.size()));
                        getLogger().info("Attempting to spawn portal to player " + selectedPlayer.getName());
                        if (portalHandler.spawnPortalNearPlayer(selectedPlayer)) {
                            getLogger().info(ChatColor.GREEN + "Attempting to spawn portal to player " + selectedPlayer.getName());
                        } else {
                            getLogger().info(ChatColor.RED + "Portal didn't spawn to player " + selectedPlayer.getName());
                        }
                    }
                }
            }
        };
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
        parser.addCommand("setprobability", new SetProbabilityCommand());
        parser.addCommand("ls", new LsDungeonCommand(this.dungeonManager));
        parser.addCommand("tp", new TpDungeonCommand(this.dungeonManager));
        parser.addCommand("spawnportal", new TestPortalCommand());
        command.setExecutor(parser);
        command.setTabCompleter(parser);
    }

    public DungeonManager getDungeonManager() {
        return dungeonManager;
    }

    public DataFile getDataFile() {
        return dataFile;
    }
}
