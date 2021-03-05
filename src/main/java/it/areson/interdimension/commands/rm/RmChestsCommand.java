package it.areson.interdimension.commands.rm;

import it.areson.interdimension.commands.AresonCommand;
import it.areson.interdimension.dungeon.Dungeon;
import it.areson.interdimension.commands.CommandParserCommand;
import it.areson.interdimension.dungeon.DungeonManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

@AresonCommand("rm chests")
public class RmChestsCommand extends CommandParserCommand {
    private final DungeonManager dm;

    public RmChestsCommand(DungeonManager dm) {
        this.dm = dm;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < this.depth) {
            commandSender.sendMessage("Devi inserire un nome");
            return false;
        }
        Dungeon dungeon = this.dm.getDungeon(strings[this.depth]);
        if (dungeon == null) {
            commandSender.sendMessage("Dungeon inesistente");
            return false;
        }
        dungeon.clearChests();
        commandSender.sendMessage("Chest rimosse dal dungeon");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return StringUtil.copyPartialMatches(strings[this.depth], this.dm.getDungeonNames(), new ArrayList<>());
    }
}
