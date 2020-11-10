package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.portals.PortalsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TestPortalCommand implements CommandExecutor {

    private AresonInterdimension plugin;
    private FileConfiguration messages;

    public TestPortalCommand(AresonInterdimension plugin) {
        this.plugin = plugin;
        this.messages = plugin.messagesFile.getConfig();
        PluginCommand testportal = plugin.getCommand("testportal");
        if(testportal!=null){
            testportal.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            plugin.portalsManager.createNewPortal(player.getLocation(), null);
        }else {
            commandSender.sendMessage("Non sei un player");
        }
        return true;
    }
}
