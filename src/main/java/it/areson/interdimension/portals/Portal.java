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

    public void activate(){
        isActive = true;
        particleTaskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin,
                () -> {
                    plugin.getServer().getWorld("world").spawnParticle(
                            Particle.END_ROD,
                            location,
                            100,
                            0,1,0,
                            0.
                    );
                },
                0,
                20
        );
    }

    public void deactivate(){
        isActive = false;
        plugin.getServer().getScheduler().cancelTask(particleTaskId);
    }

    public void teleport(Player player) {
        player.teleport(location);
    }

}
