package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.utils.MessageManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetDestinationCommand implements CommandExecutor {

    private AresonInterdimension plugin;

    public SetDestinationCommand(AresonInterdimension plugin) {
        this.plugin = plugin;
        PluginCommand command = plugin.getCommand("setdestination");
        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Location location = player.getLocation();
            plugin.data.setLocation("destination", location);
            plugin.data.save();
            commandSender.sendMessage("Destinazione portali interdimensionali impostata.");
        } else {
            commandSender.sendMessage("Non sei un player");
        }
        return true;
    }
}
