package it.areson.interdimension.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetProbabilityCommand extends CommandParserCommand {
    private final List<String> suggestions = new ArrayList<>();

    public SetProbabilityCommand() {
        suggestions.add("<double value>");
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
