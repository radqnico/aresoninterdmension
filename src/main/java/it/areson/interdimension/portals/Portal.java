package it.areson.interdimension.portals;

import it.areson.interdimension.runnables.RepeatingRunnable;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Portal object.
 * Contains all the portal information needed.
 */
public class Portal {

    /**
     * Plugin instance.
     */
    private final JavaPlugin plugin;
    /**
     * Location of the portal.
     */
    private final Location location;
    /**
     * Destination of the portal.
     */
    private final Location destination;
    /**
     * List of Runnables that show particles.
     */
    private final ArrayList<RepeatingRunnable> particleRunnables;
    /**
     * List of Runnables that play sounds.
     */
    private final ArrayList<RepeatingRunnable> soundRunnables;
    /**
     * List of Player that passed the portal.
     */
    private final ArrayList<Player> whoPassed;
    /**
     * Status of the portal.
     */
    private Status status;

    /**
     * Instantiate a new (closed) portal.
     * Open it with {@link #openPortal() openPortal()}.
     * Close it with {@link #closePortal() closePortal()}.
     * Get the status with {@link #getStatus() getStatus()}
     *
     * @param plugin      Plugin instance
     * @param location    Appearing location of the portal
     * @param destination Destination of the portal
     */
    public Portal(JavaPlugin plugin, Location location, Location destination) {
        this.plugin = plugin;
        this.location = location;
        this.destination = destination;
        status = Status.CLOSED;
        whoPassed = new ArrayList<>();
        particleRunnables = new ArrayList<>();
        soundRunnables = new ArrayList<>();
        initParticleRunnables();
        initSoundRunnables();
    }

    /**
     * Get the portal's {@link Status status}.
     *
     * @return Status of the portal.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Get the portal's location.
     * Get the middle block location.
     *
     * @return Location of the portal.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Get the portal's destination.
     *
     * @return The {@link Location} of the portal's destination.
     */
    public Location getDestination() {
        return destination;
    }

    /**
     * Open the portal: make it visible and play ambient sounds.
     * Change the status to OPEN.
     */
    public void openPortal() {
        showParticles();
        playSounds();
        status = Status.OPEN;
    }

    /**
     * Close the portal: stop particles and sounds.
     * Change the status to CLOSED.
     */
    public void closePortal() {
        hideParticles();
        stopSounds();
        status = Status.CLOSED;
    }


    /**
     * Get a list of players that passed the portal.
     *
     * @return ArrayList of players that passed the portal.
     */
    public ArrayList<Player> getWhoPassed() {
        return whoPassed;
    }


    /**
     * Notify the portal that a player passed the portal and add it to the {@link #getWhoPassed() getWhoPassed()} list.
     *
     * @param player Player that passed the portal.
     * @return Returns true if it is the first player to pass the portal, false otherwise.
     */
    public boolean playerPassedPortal(Player player) {
        whoPassed.add(player);
        return whoPassed.size() == 1;
    }


    /**
     * Teleport all passed players to the appearing location of the portal (works independently from the status).
     * Also empties the passed players list.
     */
    public void returnBackWhoPassed() {
        final int size = whoPassed.size();
        for (int i = 0; i < size; i++) {
            whoPassed.remove(0).teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    /**
     * Plays the teleport effects particles and sound
     */
    public void playTeleportEffects(){
        playTeleportSound();
        spawnTeleportParticles();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Portal)) return false;
        Portal portal = (Portal) o;
        return plugin.equals(portal.plugin) && location.equals(portal.location) && destination.equals(portal.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugin, location, destination);
    }

    // Private

    private void spawnTeleportParticles() {
        location.getWorld().spawnParticle(
                Particle.END_ROD,
                location,
                600,
                .2, 1, .2,
                1
        );
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                plugin,
                () -> destination.getWorld().spawnParticle(
                        Particle.END_ROD,
                        destination,
                        600,
                        .2, 1, .2,
                        1
                ),
                5
        );
    }

    private void playTeleportSound() {
        location.getWorld().playSound(
                location,
                Sound.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.MASTER,
                1f,
                0.6f
        );
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                plugin,
                () -> destination.getWorld().playSound(
                        destination,
                        Sound.ENTITY_ENDERMAN_TELEPORT,
                        SoundCategory.MASTER,
                        1f,
                        0.6f
                ),
                5
        );

    }

    private void playSounds() {
        for (RepeatingRunnable soundRunnable : soundRunnables) {
            soundRunnable.runRepeatingTask();
        }
    }

    private void stopSounds() {
        stopSoundRunnables();
        initSoundRunnables();
    }

    private void showParticles() {
        for (RepeatingRunnable particleRunnable : particleRunnables) {
            particleRunnable.runRepeatingTask();
        }
    }

    private void hideParticles() {
        stopParticleRunnables();
        initParticleRunnables();
    }

    private void stopSoundRunnables() {
        final int size = soundRunnables.size();
        for (int i = 0; i < size; i++) {
            soundRunnables.remove(0).stopRepeatingTask();
        }
    }

    private void initSoundRunnables() {
        soundRunnables.add(new RepeatingRunnable(plugin, 0, 80) {
            @Override
            public void run() {
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
            }
        });
    }

    private void stopParticleRunnables() {
        final int size = particleRunnables.size();
        for (int i = 0; i < size; i++) {
            particleRunnables.remove(0).stopRepeatingTask();
        }
    }

    private void initParticleRunnables() {
        particleRunnables.add(new RepeatingRunnable(plugin, 0, 2) {
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

    public enum Status {
        OPEN,
        CLOSED
    }

}
