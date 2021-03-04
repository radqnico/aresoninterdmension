package it.areson.interdimension.runnables;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Wrapper for BukkitRunnable that hold the information to start and stop a repeating task.
 */
public abstract class RepeatingRunnable extends BukkitRunnable {

    /**
     * Delay for the repeating task.
     */
    private final long delay;
    /**
     * Interval from one repetition to another.
     */
    private final long interval;
    /**
     * Plugin instance.
     */
    private final JavaPlugin plugin;

    /**
     * Create a new Repeating Runnable.
     *
     * @param plugin   Plugin instance.
     * @param delay    Delay of the first repetition.
     * @param interval Interval between repetitions.
     */
    public RepeatingRunnable(JavaPlugin plugin, long delay, long interval) {
        this.plugin = plugin;
        this.delay = delay;
        this.interval = interval;
    }

    /**
     * Stop repeating this task.
     * <b>The task cannot be resumed.</b>
     */
    public void stopRepeatingTask() {
        if (!this.isCancelled()) {
            this.cancel();
        }
    }

    /**
     * Run this repeating task.
     * <b>Stop it with {@link #stopRepeatingTask() stopRepeatingTask()}</b>
     */
    public void runRepeatingTask() {
        this.runTaskTimer(plugin, delay, interval);
    }

    @Override
    public abstract void run();
}
