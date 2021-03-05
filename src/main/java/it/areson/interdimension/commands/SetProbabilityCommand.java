package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.Configuration;
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
        if (strings.length == 2) {
            try {
                AresonInterdimension instance = AresonInterdimension.getInstance();
                double newProbability = Double.parseDouble(strings[1]);
                instance.getDataFile().setPortalProbability(newProbability);
                instance.getDataFile().save();
                commandSender.sendMessage(
                        String.format(
                                "Probability to have:\n > 1 portal per night: %f\n > 2 portal per night: %f\n > 3 portal per night: %f",
                                Configuration.getProbabilityOfPortals(1, newProbability),
                                Configuration.getProbabilityOfPortals(2, newProbability),
                                Configuration.getProbabilityOfPortals(3, newProbability)
                        )
                );
            } catch (NumberFormatException exception) {
                commandSender.sendMessage("Not a number.");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return this.suggestions;
    }
}
