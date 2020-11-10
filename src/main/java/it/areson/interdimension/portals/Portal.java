package it.areson.interdimension.portals;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Portal {

    private JavaPlugin plugin;
    private Location location;
    private Location destination;
    private int particleTaskId;
    private boolean isActive;

    public Portal(JavaPlugin plugin, Location location, Location destination) {
        this.plugin = plugin;
        this.location = location;
        this.destination = destination;
        this.isActive = false;
    }

    public void activate() {
        isActive = true;
        particleTaskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin,
                () -> {
                    plugin.getServer().getWorld("world").spawnParticle(
                            Particle.END_ROD,
                            location,
                            5,
                            .2, 1, .2,
                            0.01
                    );
                    plugin.getServer().getWorld("world").spawnParticle(
                            Particle.REVERSE_PORTAL,
                            location,
                            5,
                            .2, 1, .2,
                            0.01
                    );
                    plugin.getServer().getWorld("world").spawnParticle(
                            Particle.PORTAL,
                            location,
                            10,
                            .2, 1, .2,
                            0.1
                    );
                    plugin.getServer().getWorld("world").spawnParticle(
                            Particle.DRIPPING_OBSIDIAN_TEAR,
                            location,
                            2,
                            .2, 1, .2,
                            0.1
                    );
                },
                0,
                2
        );
    }

    public void deactivate() {
        isActive = false;
        plugin.getServer().getScheduler().cancelTask(particleTaskId);
    }

    public void teleport(Player player) {
        player.teleport(location);
    }

}
