package it.areson.interdimension.portals;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Portal {

    private AresonInterdimension plugin;
    private Location location;
    private Location destination;
    private int particleTaskId;
    private int timeoutTaskId;
    private int soundTask;
    private boolean isActive;
    private int secondsTimeout;

    public Portal(AresonInterdimension plugin, Location location, Location destination, int secondsTimeout) {
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
        plugin.portalManager.removePortal();
    }

    public void spark(){
        location.getWorld().spawnParticle(
                Particle.END_ROD,
                location,
                100,
                .2, 1, .2,
                1
        );
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                plugin,
                () -> {
                    destination.getWorld().spawnParticle(
                            Particle.END_ROD,
                            destination,
                            100,
                            .2, 1, .2,
                            1
                    );
                },
                2
        );
    }

    public void teleport(Player player) {
        player.teleport(destination);
        location.getWorld().playSound(
                location,
                Sound.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.MASTER,
                1f,
                0.6f
        );
        player.getWorld().playSound(
                destination,
                Sound.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.MASTER,
                1f,
                0.6f
        );
        player.getWorld().spawnParticle(
                Particle.END_ROD,
                location,
                100,
                .2, .2, .2,
                1
        );
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
