package it.areson.interdimension.portals;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Portal {

    private JavaPlugin plugin;
    private Location location;
    private int durationSeconds;
    private Player whoPassed;
    private Status status;
    private ArrayList<BukkitRunnable> particleRunnables;
    private ArrayList<BukkitRunnable> soundRunnables;


    private void stopAllSoundRunnables() {
        for (int i = 0; i < soundRunnables.size(); i++) {
            if (!soundRunnables.get(0).isCancelled()) {
                soundRunnables.get(0).cancel();
            }
            soundRunnables.remove(0);
        }
    }

    private void initSoundRunnables() {
        stopAllSoundRunnables();

        soundRunnables.add(new BukkitRunnable() {
            @Override
            public void run() {

            }
        });
    }

    public void playSounds() {
        for (BukkitRunnable soundRunnable : soundRunnables) {
            soundRunnable.runTaskTimer(plugin, 0, 2);
        }
    }

    private void stopAllParticleRunnables() {
        for (int i = 0; i < particleRunnables.size(); i++) {
            if (!particleRunnables.get(0).isCancelled()) {
                particleRunnables.get(0).cancel();
            }
            particleRunnables.remove(0);
        }
    }

    private void initParticleRunnables() {
        stopAllParticleRunnables();

        particleRunnables.add(new BukkitRunnable() {
            @Override
            public void run() {
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
            }
        });
    }

    public void showParticles() {
        for (BukkitRunnable particleRunnable : particleRunnables) {
            particleRunnable.runTaskTimer(plugin, 0, 2);
        }
    }

    public enum Status {
        OPEN,
        CLOSED,
        PASSED,
        EXPIRED
    }

}
