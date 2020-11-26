package it.areson.interdimension.portals;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.events.PlayerEvents;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Optional;

public class PortalManager {

    private Portal activePortal;
    private AresonInterdimension plugin;
    private PlayerEvents playerEvents;
    private boolean portalPassed;
    private int goBackTaskId;
    private Player player;

    public PortalManager(AresonInterdimension plugin, PlayerEvents playerEvents) {
        this.playerEvents = playerEvents;
        this.activePortal = null;
        this.plugin = plugin;
        this.portalPassed = false;
    }

    public boolean createNewPortal(Location location, Location destination, int secondsTimeout) {
        if (Objects.isNull(activePortal) && !portalPassed) {
            activePortal = new Portal(plugin, location, destination, secondsTimeout);
            activePortal.activate();
            playerEvents.registerEvents();
            return true;
        }
        return false;
    }

    public void removePortal() {
        if (Objects.nonNull(activePortal)) {
            if(activePortal.isActive()) {
                activePortal.deactivate();
            }
            activePortal = null;
            playerEvents.unregisterEvents();
        }
    }

    public void setPortalPassed(boolean portalPassed, Player player) {
        this.portalPassed = portalPassed;
        this.player = player;
    }

    public boolean isPassedPlayer(Player player){
        return this.player.getName().equals(player.getName());
    }

    public void startGoBackTask(final Location location){
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                plugin,
                () -> {
                    player.teleport(location);
                    location.getWorld().spawnParticle(
                            Particle.END_ROD,
                            location,
                            500,
                            .2, 1, .2,
                            1
                    );
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                            plugin,
                            () -> {
                                location.getWorld().spawnParticle(
                                        Particle.END_ROD,
                                        location,
                                        500,
                                        .2, 1, .2,
                                        1
                                );
                                location.getWorld().playSound(
                                        location,
                                        Sound.ENTITY_ENDERMAN_TELEPORT,
                                        SoundCategory.MASTER,
                                        1f,
                                        0.6f
                                );
                            },
                            2
                    );
                    setPortalPassed(false, null);
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                },
                600
        );
    }

    public Optional<Portal> getActivePortal() {
        return Objects.nonNull(activePortal) ? Optional.of(activePortal) : Optional.empty();
    }
}
