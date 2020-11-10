package it.areson.interdimension.portals;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Portal {

    private JavaPlugin plugin;
    private Location location;
    private Location destination;
    private int particleTaskId;
    private int timeoutTaskId;
    private int soundTask;
    private boolean isActive;
    private int secondsTimeout;

    public Portal(JavaPlugin plugin, Location location, Location destination, int secondsTimeout) {
        this.plugin = plugin;
        this.location = location;
        this.destination = destination;
        this.secondsTimeout = secondsTimeout;
        this.isActive = false;
    }

    public void activate() {
        isActive = true;
        timeoutTaskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                plugin,
                this::deactivate,
                secondsTimeout*20
        );
        soundTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin,
                () -> {
                    plugin.getServer().getWorld("world").playSound(
                            location,
                            Sound.AMBIENT_NETHER_WASTES_MOOD,
                            SoundCategory.MASTER,
                            1f,
                            0.6f
                    );
                },
                0,
                50
        );
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

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
        plugin.getServer().getScheduler().cancelTask(particleTaskId);
        plugin.getServer().getScheduler().cancelTask(timeoutTaskId);
        plugin.getServer().getScheduler().cancelTask(soundTask);
    }

    public void teleport(Player player) {
        player.teleport(location);
    }

}
