package it.areson.interdimension.portals;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.events.PlayerPassPortalEvents;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;

public class PortalManager {

    private Portal activePortal;
    private AresonInterdimension plugin;
    private PlayerPassPortalEvents playerPassPortalEvents;
    private boolean portalPassed;
    private int goBackTaskId;

    public PortalManager(AresonInterdimension plugin, PlayerPassPortalEvents playerPassPortalEvents) {
        this.playerPassPortalEvents = playerPassPortalEvents;
        this.activePortal = null;
        this.plugin = plugin;
        this.portalPassed = false;
    }

    public boolean createNewPortal(Location location, Location destination, int secondsTimeout) {
        if (Objects.isNull(activePortal) && !portalPassed) {
            activePortal = new Portal(plugin, location, destination, secondsTimeout);
            activePortal.activate();
            playerPassPortalEvents.registerEvents();
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
            playerPassPortalEvents.unregisterEvents();
        }
    }

    public void setPortalPassed(boolean portalPassed) {
        this.portalPassed = portalPassed;
    }

    public void startGoBackTask(final Location location, final Player player){
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
                    setPortalPassed(false);
                },
                600
        );
    }

    public Optional<Portal> getActivePortal() {
        return Objects.nonNull(activePortal) ? Optional.of(activePortal) : Optional.empty();
    }
}
