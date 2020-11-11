package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
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
    private FileConfiguration messages;

    public SetDestinationCommand(AresonInterdimension plugin) {
        this.plugin = plugin;
        this.messages = plugin.messagesFile.getConfig();
        PluginCommand command = plugin.getCommand("setdestination");
        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            checkSection();
            Location location = player.getLocation();
            ConfigurationSection destination = plugin.getConfig().getConfigurationSection("destination");
            if(destination!=null){
                destination.set("world",location.getWorld().getName());
                destination.set("x",location.getX());
                destination.set("y",location.getY());
                destination.set("z",location.getZ());
                destination.set("yaw",location.getYaw());
                destination.set("pitch",location.getPitch());
                plugin.saveConfig();
                commandSender.sendMessage("Destinazione portali interdimensionali impostata.");
            }else{
                commandSender.sendMessage("Destinazione portali: errore nel file.");
            }
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
