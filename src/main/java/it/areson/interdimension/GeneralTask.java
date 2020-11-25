package it.areson.interdimension;

import it.areson.interdimension.utils.ConfigValidator;
import it.areson.interdimension.utils.PortalLocationFinder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class GeneralTask {

    private boolean isRunning;
    private int taskId = -1;
    private AresonInterdimension plugin;

    public GeneralTask(AresonInterdimension plugin) {
        this.plugin = plugin;
        isRunning = false;
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
        isRunning = true;
        plugin.getLogger().info("Interdimensional Portals task started");
    }


    public void trySpawnPortal() {
        if (!plugin.portalManager.getActivePortal().isPresent()) {
            Optional<Location> chestOptional = plugin.data.getLocation("chest");
            if (chestOptional.isPresent()) {
                Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();
                List<Player> users = onlinePlayers.stream().filter(player -> (player.getGameMode().equals(GameMode.SURVIVAL)) && (!player.isDead())).collect(Collectors.toList());
                int size = users.size();
                if (size > 0) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(size);
                    Player selectedPlayer = users.get(randomIndex);
                    if (ConfigValidator.isProbabilityValid()) {
                        double probability = plugin.data.getFileConfiguration().getDouble("spawn-probability-every-five-seconds");
                        if (random.nextDouble() < probability) {
                            Optional<Location> destinationOptional = plugin.data.getLocation("destination");
                            if (destinationOptional.isPresent()) {
                                Location destination = destinationOptional.get();
                                Location optimalLocationForPortal = PortalLocationFinder.findOptimalLocationForPortal(selectedPlayer);
                                boolean newPortalIsCreated = plugin.portalManager.createNewPortal(optimalLocationForPortal, destination, 120);
                                if (newPortalIsCreated) {
                                    Location chestLocation = chestOptional.get();
                                    emptyChest(chestLocation);
                                    plugin.getServer().getLogger().info(plugin.messages.getPlainMessage("console-log-portal-spawned")
                                            .replaceAll("%player%", selectedPlayer.getName()));
                                    selectedPlayer.playSound(selectedPlayer.getLocation(), Sound.AMBIENT_NETHER_WASTES_MOOD, SoundCategory.MASTER, 1, 0.7f);
                                }
                            } else {
                                plugin.getLogger().severe(plugin.messages.getPlainMessage("destination-not-set"));
                            }
                        }
                    } else {
                        plugin.getLogger().warning(plugin.messages.getPlainMessage("probability-not-valid"));
                    }
                }
            } else {
                plugin.getLogger().warning(plugin.messages.getPlainMessage("chest-not-set"));
            }
        }
    }

    public void emptyChest(Location location) {
        Block block = location.getBlock();
        block.setType(Material.CHEST);
        BlockState blockState = block.getState();
        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            chest.getInventory().clear();
            setChestLootTable(location);
        }
    }

    public void setChestLootTable(Location location) {
        BlockState blockState = location.getBlock().getState();
        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            chest.setLootTable(LootTables.END_CITY_TREASURE.getLootTable());
            chest.setNextRefill(Long.MAX_VALUE);
            chest.update();
        }
    }

    public void stopTask() {
        if (isRunning) {
            isRunning = false;
            plugin.getServer().getScheduler().cancelTask(taskId);
            plugin.getLogger().info("Interdimensional Portals task stopped");
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
