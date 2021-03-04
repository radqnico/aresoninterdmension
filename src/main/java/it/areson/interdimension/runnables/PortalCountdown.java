package it.areson.interdimension.runnables;

import it.areson.interdimension.portals.Portal;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper of BukkitRunnable to start a simple delayed task.
 */
public class PortalCountdown {

    /**
     * Static list of {@link PortalCountdownEndListener} to notify the end to.
     */
    private static List<PortalCountdownEndListener> listeners;
    /**
     * This countdown's connected portal.
     */
    private final Portal portal;
    /**
     * Seconds of the countdown.
     */
    private final int seconds;
    /**
     * Runnable that will be delayed.
     */
    private final BukkitRunnable runnable;
    /**
     * Instance of the plugin.
     */
    private final JavaPlugin plugin;

    /**
     * Create a new countdown, not started by default.
     *
     * @param plugin  Plugin instance.
     * @param seconds Seconds of the countdown.
     * @param portal  Portal connected to this countdown.
     */
    public PortalCountdown(JavaPlugin plugin, int seconds, Portal portal) {
        this.seconds = seconds;
        this.portal = portal;
        this.plugin = plugin;
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                notifyAllListeners();
            }
        };
    }

    /**
     * Register a new {@link PortalCountdownEndListener listener} to notify countdown finishes.
     *
     * @param portalCountdownEndListener Listener to register.
     */
    public static void registerListener(PortalCountdownEndListener portalCountdownEndListener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(portalCountdownEndListener);
    }

    /**
     * Notify all registered listeners that the countdown has finished.
     */
    private void notifyAllListeners() {
        for (PortalCountdownEndListener listener : listeners) {
            listener.notifyCountdownFinish(portal);
        }
    }

    /**
     * Starts the countdown.
     */
    public void startCountdown() {
        runnable.runTaskLater(plugin, seconds * 20L);
    }

    /**
     * Interrupts the countdown (and notifies the listeners).
     */
    public void interruptCountdown() {
        if (!runnable.isCancelled()) {
            runnable.cancel();
            notifyAllListeners();
        }
    }

}
