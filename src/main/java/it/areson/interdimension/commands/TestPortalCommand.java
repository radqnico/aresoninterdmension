package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestPortalCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            System.out.println(AresonInterdimension.getInstance().getPortalHandler().spawnPortalNearPlayer(((Player) commandSender)));
        }
        return true;
    }
}
