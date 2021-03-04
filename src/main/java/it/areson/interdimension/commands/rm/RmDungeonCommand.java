package it.areson.interdimension.commands.rm;

import it.areson.interdimension.commands.ICommandParserCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class RmDungeonCommand implements ICommandParserCommand {
    private final List<String> suggestions = new ArrayList<>();

    public RmDungeonCommand() {
        suggestions.add("<name>");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return this.suggestions;
    }
}
