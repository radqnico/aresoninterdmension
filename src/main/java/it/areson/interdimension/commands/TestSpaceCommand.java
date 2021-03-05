package it.areson.interdimension.commands;

import it.areson.interdimension.locationfinder.LocationFinder;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AresonCommand("testspace")
public class TestSpaceCommand extends CommandParserCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Location location = player.getEyeLocation().toCenterLocation();
            player.sendMessage(location.toString());
            player.sendMessage(LocationFinder.checkThreeBlockAirSpace(location) + "");
            player.sendMessage(location.getBlock().getType().name());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
