package it.areson.interdimension.commands;

import it.areson.interdimension.dungeon.DungeonManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@AresonCommand("ls")
public class LsDungeonCommand extends CommandParserCommand {
    private final DungeonManager dm;

    public LsDungeonCommand(DungeonManager dm) {
        this.dm = dm;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (String name : this.dm.getDungeonNames()) {
            commandSender.sendMessage(name);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
