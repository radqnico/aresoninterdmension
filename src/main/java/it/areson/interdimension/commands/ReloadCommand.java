package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadCommand implements CommandExecutor {

    private AresonInterdimension plugin;
    private FileConfiguration messages;

    public ReloadCommand(AresonInterdimension plugin) {
        this.plugin = plugin;
        this.messages = plugin.messagesFile.getConfig();
        PluginCommand testportal = plugin.getCommand("interdimreload");
        if (testportal != null) {
            testportal.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        plugin.reloadConfig();
        plugin.reloadAllConfigs();
        commandSender.sendMessage(plugin.messagesFile.getConfig().getString("reloaded"));
        return true;
    }
}
