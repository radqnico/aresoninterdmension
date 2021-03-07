package it.areson.interdimension.dungeon;

import it.areson.interdimension.AresonInterdimension;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;

public class ChestContent {

    public static void emptyChest(Location location) {
        Block block = location.getBlock();
        block.setType(Material.CHEST);
        BlockState blockState = block.getState();
        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            chest.getInventory().clear();
            setChestLootTable(location);
        }
    }

    public static void setChestLootTable(Location location) {
        BlockState blockState = location.getBlock().getState();
        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            NamespacedKey namespacedKey = NamespacedKey.minecraft("chests/woodland_mansion");
            chest.setLootTable(AresonInterdimension.getInstance().getServer().getLootTable(namespacedKey));
            chest.update();
        }
    }

}
