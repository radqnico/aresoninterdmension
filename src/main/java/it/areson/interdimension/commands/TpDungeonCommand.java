package it.areson.interdimension.commands;

import it.areson.interdimension.dungeon.Dungeon;
import it.areson.interdimension.dungeon.DungeonManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TpDungeonCommand extends CommandParserCommand {
    private final DungeonManager dm;

    public TpDungeonCommand(DungeonManager dm) {
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
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.teleport(dungeon.getLocation());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return StringUtil.copyPartialMatches(strings[this.depth], this.dm.getDungeonNames(), new ArrayList<>());
    }
}
