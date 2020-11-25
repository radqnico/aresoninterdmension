package it.areson.interdimension.commands;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.loot.Lootable;

public class SetPortalChestCommand implements CommandExecutor {

    private AresonInterdimension plugin;

    public SetPortalChestCommand(AresonInterdimension plugin) {
        this.plugin = plugin;
        PluginCommand command = plugin.getCommand("setportalchest");
        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Block targetBlockExact = player.getTargetBlockExact(100);
            if(targetBlockExact!=null){
                BlockState state = targetBlockExact.getState();
                if(state instanceof Chest){
                    Chest chest = (Chest) state;
                    chest.setLootTable(LootTables.END_CITY_TREASURE.getLootTable());
                    chest.setNextRefill(600);
                    player.sendMessage("Chest impostata.");
                }else{
                    player.sendMessage("Guarda una chest");
                }
            }else{
                player.sendMessage("Guarda una chest");
            }
        } else {
            commandSender.sendMessage("Non sei un player");
        }
        return true;
    }

}
