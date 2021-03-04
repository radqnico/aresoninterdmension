package it.areson.interdimension.commands.rm;

import it.areson.interdimension.commands.CommandParserCommand;
import it.areson.interdimension.dungeon.DungeonManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class RmDungeonCommand extends CommandParserCommand {
    private final DungeonManager dm;

    public RmDungeonCommand(DungeonManager dm) {
        this.dm = dm;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < this.depth) {
            return false;
        }
        return this.dm.removeDungeon(strings[this.depth]);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return StringUtil.copyPartialMatches(strings[this.depth], this.dm.getDungeonNames(), new ArrayList<>());
    }
}
