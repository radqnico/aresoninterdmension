package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetDestinationCommand implements CommandExecutor {

    private AresonInterdimension plugin;
    private FileConfiguration messages;

    public SetDestinationCommand(AresonInterdimension plugin) {
        this.plugin = plugin;
        this.messages = plugin.messagesFile.getConfig();
        PluginCommand testportal = plugin.getCommand("setdestination");
        if (testportal != null) {
            testportal.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            checkSection();
            Location location = player.getLocation();
            plugin.getConfig().getConfigurationSection("destination").set("x",location.getX());
            plugin.getConfig().getConfigurationSection("destination").set("y",location.getY());
            plugin.getConfig().getConfigurationSection("destination").set("z",location.getZ());
            plugin.getConfig().getConfigurationSection("destination").set("y",location.getYaw());
            plugin.getConfig().getConfigurationSection("destination").set("p",location.getPitch());
            commandSender.sendMessage("Destinazione portali interdimensionali impostata.");
        } else {
            commandSender.sendMessage("Non sei un player");
        }
        return true;
    }

    public void checkSection(){
        if(!plugin.getConfig().isConfigurationSection("destination")){
            plugin.getConfig().createSection("destination");
        }
    }
}
