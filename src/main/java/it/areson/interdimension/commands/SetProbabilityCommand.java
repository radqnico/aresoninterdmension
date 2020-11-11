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

public class SetProbabilityCommand implements CommandExecutor {

    private AresonInterdimension plugin;
    private FileConfiguration messages;

    public SetProbabilityCommand(AresonInterdimension plugin) {
        this.plugin = plugin;
        this.messages = plugin.messagesFile.getConfig();
        PluginCommand command = plugin.getCommand("setprobability");
        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if(strings.length == 1){
                try {
                    double probability = Double.parseDouble(strings[0]);
                    FileConfiguration config = plugin.getConfig();
                    config.set("spawn-probability-every-five-seconds", probability);
                    plugin.saveConfig();
                    plugin.portalManager.setProbability(probability);
                    commandSender.sendMessage("Destinazione portali interdimensionali impostata.");
                }catch (NumberFormatException ex){
                    commandSender.sendMessage("Inserisci un numero decimale.");
                }
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
