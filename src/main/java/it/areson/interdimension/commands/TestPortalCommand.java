package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
import it.areson.interdimension.portals.PortalsManager;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TestPortalCommand implements CommandExecutor {

    private AresonInterdimension plugin;
    private FileConfiguration messages;

    public TestPortalCommand(AresonInterdimension plugin) {
        this.plugin = plugin;
        this.messages = plugin.messagesFile.getConfig();
        PluginCommand testportal = plugin.getCommand("testportal");
        if (testportal != null) {
            testportal.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();
        List<Player> users = onlinePlayers.stream().filter(player -> (!player.isOp())&&(player.getGameMode().equals(GameMode.SURVIVAL))).collect(Collectors.toList());
        int size = users.size();
        Random random = new Random();
        int randomIndex = random.nextInt(size);
        Player selectedPlayer = users.get(randomIndex);
        double probability = plugin.getConfig().getDouble("probability");
        if(random.nextDouble()<probability){
            plugin.portalsManager.createNewPortal(selectedPlayer.getLocation(), null, 20);
            plugin.getServer().broadcastMessage("Portale non spawnato a " + selectedPlayer.getName());
        }else{
            plugin.getServer().broadcastMessage("Portale spawnato a " + selectedPlayer.getName());
        }
        return true;
    }
}
