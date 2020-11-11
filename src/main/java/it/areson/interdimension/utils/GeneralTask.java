package it.areson.interdimension.utils;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GeneralTask {

    private int taskId = -1;
    private AresonInterdimension plugin;

    public GeneralTask(AresonInterdimension plugin) {
        this.plugin = plugin;

    }

    public void startTask() {
        taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin,
                () -> {
                    long time = plugin.portalsWorld.getTime();
                    if (time > 13000 && time < 23000) {
                        trySpawnPortal();
                    }
                },
                0,
                100
        );
        plugin.getLogger().info("Interdimensional Portals task started!");
    }


    public void trySpawnPortal() {
        Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();
        List<Player> users = onlinePlayers.stream().filter(player -> (player.getGameMode().equals(GameMode.SURVIVAL)) && (!player.isDead())).collect(Collectors.toList());
        int size = users.size();
        if (size > 0) {
            Random random = new Random();
            int randomIndex = random.nextInt(size);
            Player selectedPlayer = users.get(randomIndex);
            double probability = plugin.portalManager.getProbability();
            if (random.nextDouble() < probability) {
                Location destination = plugin.portalManager.getDestination();
                Location optimalLocationForPortal = PortalLocationFinder.findOptimalLocationForPortal(selectedPlayer);
                plugin.portalManager.createNewPortal(optimalLocationForPortal, destination, 20);
                plugin.getServer().getLogger().info("Portal spawned at player '" + selectedPlayer.getName() + "'");
            }
        }
    }

    public void stopTask() {
        if (taskId != -1) {
            plugin.getServer().getScheduler().cancelTask(taskId);
            plugin.getLogger().info("Interdimensional Portals task stopped!");
        }
    }
}
