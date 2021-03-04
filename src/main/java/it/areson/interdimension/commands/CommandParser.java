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

public class CommandParser implements ICommandParserCommand {
    private final JavaPlugin plugin;
    private final HashMap<String, ICommandParserCommand> commands = new HashMap<>();
    private int depth = 0;

    public CommandParser(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > 0) {
            CommandExecutor selectedCommand = commands.get(strings[0].toLowerCase());
            if (selectedCommand != null) {
                selectedCommand.onCommand(commandSender, command, s, strings);
                return true;
            }
        }
        return false;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void addCommand(String arg, CommandParser parser) {
        parser.setDepth(this.depth + 1);
        plugin.getLogger().log(Level.WARNING, "Binding depth " + (this.depth + 1) + " command");
        this.addCommand(arg, (ICommandParserCommand) parser);
    }
    public void addCommand(String arg, ICommandParserCommand executor) {
        if (this.commands.containsKey(arg)) {
            plugin.getLogger().log(Level.WARNING, "Already insert " + arg + " command");
            return;
        }
        this.commands.put(arg, executor);
        plugin.getLogger().log(Level.INFO, "Bind " + arg + " command");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > this.depth + 1) {
            ICommandParserCommand selectedCommand = commands.get(strings[this.depth].toLowerCase());
            if (selectedCommand != null) {
                return selectedCommand.onTabComplete(commandSender, command, s, strings);
            }
            return StringUtil.copyPartialMatches(strings[this.depth], this.commands.keySet(), new ArrayList<>());
        }
        return new ArrayList<>(this.commands.keySet());
    }
}
