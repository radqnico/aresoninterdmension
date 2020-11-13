package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class TogglePortalsCommand implements CommandExecutor {

    private AresonInterdimension plugin;

    public TogglePortalsCommand(AresonInterdimension plugin) {
        this.plugin = plugin;
        PluginCommand command = plugin.getCommand("toggleportals");
        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (plugin.generalTask.isRunning()) {
            plugin.generalTask.stopTask();
            plugin.portalManager.removePortal();
            commandSender.sendMessage(plugin.messages.getPlainMessage("portals-deactivated"));
        } else {
            plugin.generalTask.startTask();
            commandSender.sendMessage("Interdimensional Portals task started!");
        }
        return true;
    }
}
