package it.areson.interdimension.portals;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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
                    location.getWorld().playSound(
                            location,
                            Sound.AMBIENT_NETHER_WASTES_MOOD,
                            SoundCategory.MASTER,
                            1f,
                            0.6f
                    );
                    location.getWorld().playSound(
                            location,
                            Sound.AMBIENT_CRIMSON_FOREST_MOOD,
                            SoundCategory.MASTER,
                            1f,
                            0.6f
                    );
                },
                0,
                80
        );
        particleTaskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin,
                () -> {
                    location.getWorld().spawnParticle(
                            Particle.END_ROD,
                            location,
                            3,
                            .2, 1, .2,
                            0.01
                    );
                    location.getWorld().spawnParticle(
                            Particle.REVERSE_PORTAL,
                            location,
                            10,
                            .2, 1, .2,
                            0.01
                    );
                    location.getWorld().spawnParticle(
                            Particle.PORTAL,
                            location,
                            20,
                            .2, 1, .2,
                            0.1
                    );
                    location.getWorld().spawnParticle(
                            Particle.DRIPPING_OBSIDIAN_TEAR,
                            location,
                            4,
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

    public void spark(){
        plugin.getServer().getWorld("world").spawnParticle(
                Particle.END_ROD,
                location,
                100,
                .2, 1, .2,
                1
        );
    }

    public void teleport(Player player) {
        player.teleport(location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Portal)) return false;
        Portal portal = (Portal) o;
        return Objects.equals(location, portal.location);
    }

    public Chunk getPortalChunk(){
        return location.getChunk();
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    public Location getLocation() {
        return location;
    }
}
