package it.areson.interdimension.commands.add;

import it.areson.interdimension.commands.ICommandParserCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class AddDungeonCommand implements ICommandParserCommand {
    private final List<String> suggestions = new ArrayList<>();

    public AddDungeonCommand() {
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
