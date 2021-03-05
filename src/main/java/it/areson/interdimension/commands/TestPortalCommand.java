package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TestPortalCommand extends CommandParserCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            AresonInterdimension instance = AresonInterdimension.getInstance();
            Player player = (Player) commandSender;
            if (instance.getPortalHandler().spawnPortalNearPlayer(player)) {
                instance.getLogger().info(ChatColor.GREEN + "Attempting to spawn portal to palyer " + player.getName());
            } else {
                instance.getLogger().info(ChatColor.RED + "Portal didn't spawn to player " + player.getName());
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
