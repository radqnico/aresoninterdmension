package it.areson.interdimension.commands.add;

import it.areson.interdimension.dungeon.Dungeon;
import it.areson.interdimension.commands.CommandParserCommand;
import it.areson.interdimension.dungeon.DungeonManager;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AddChestCommand extends CommandParserCommand {
    private final DungeonManager dm;

    public AddChestCommand(DungeonManager dm) {
        this.dm = dm;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < this.depth) {
            return false;
        }
        Dungeon dungeon = this.dm.getDungeon(strings[this.depth]);
        if (dungeon == null) {
            return false;
        }
        Player player = (Player) commandSender;
        Block targetBlockExact = player.getTargetBlockExact(100);
        if (targetBlockExact != null) {
            BlockState state = targetBlockExact.getState();
            if (state instanceof Chest) {
                dungeon.addChest(targetBlockExact.getLocation());
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return StringUtil.copyPartialMatches(strings[this.depth], this.dm.getDungeonNames(), new ArrayList<>());
    }
}
