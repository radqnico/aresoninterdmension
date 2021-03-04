package it.areson.interdimension.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class CommandParser extends CommandParserCommand {
    private final JavaPlugin plugin;
    private final HashMap<String, CommandParserCommand> commands = new HashMap<>();

    public CommandParser(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > 0) {
            CommandExecutor selectedCommand = commands.get(strings[0].toLowerCase());
            if (selectedCommand != null) {
                return selectedCommand.onCommand(commandSender, command, s, strings);
            }
        }
        return false;
    }

    public void addCommand(String arg, CommandParserCommand executor) {
        if (this.commands.containsKey(arg)) {
            plugin.getLogger().log(Level.WARNING, "Already insert " + arg + " command");
            return;
        }
        executor.setDepth(this.depth + 1);
        this.commands.put(arg, executor);
        plugin.getLogger().log(Level.INFO, "Bind " + arg + " command");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > this.depth + 1) {
            CommandParserCommand selectedCommand = commands.get(strings[this.depth].toLowerCase());
            if (selectedCommand != null) {
                return selectedCommand.onTabComplete(commandSender, command, s, strings);
            }
            return StringUtil.copyPartialMatches(strings[this.depth], this.commands.keySet(), new ArrayList<>());
        }
        return new ArrayList<>(this.commands.keySet());
    }
}
