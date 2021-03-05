package it.areson.interdimension.commands.add;

import it.areson.interdimension.commands.AresonCommand;
import it.areson.interdimension.dungeon.Dungeon;
import it.areson.interdimension.commands.CommandParserCommand;
import it.areson.interdimension.dungeon.DungeonManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AresonCommand("add dungeon")
public class AddDungeonCommand extends CommandParserCommand {
    private final List<String> suggestions = new ArrayList<>();
    private final DungeonManager dm;

    public AddDungeonCommand(DungeonManager dm) {
        this.dm = dm;
        suggestions.add("<name>");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < this.depth) {
            commandSender.sendMessage("Devi inserire un nome");
            return false;
        }
        Dungeon newDungeon = new Dungeon(strings[this.depth]);
        Player player = (Player) commandSender;
        newDungeon.setLocation(player.getLocation());
        if (this.dm.addDungeon(newDungeon)) {
            commandSender.sendMessage("Dungeon settato");
            return true;
        }
        commandSender.sendMessage("Impossibile aggiungere il dungeon");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return this.suggestions;
    }
}
