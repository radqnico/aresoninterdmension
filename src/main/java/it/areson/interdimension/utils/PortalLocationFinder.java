package it.areson.interdimension.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PortalLocationFinder {

    public static Location findOptimalLocationForPortal(Player player){
        Location playerLocation = player.getLocation().clone();
        Location behind = playerLocation.clone().subtract(playerLocation.getDirection().multiply(2));
        Location testLocation = behind.clone();
        while(testLocation.getBlock().getType().equals(Material.AIR) &&
                testLocation.clone().subtract(0,1,0).getBlock().getType().equals(Material.AIR) &&
                testLocation.getY()<=200){
            testLocation.add(0,3,0);
        }
        if(testLocation.getY()>=200){
            testLocation = behind.clone();
            while(testLocation.getBlock().getType().equals(Material.AIR) && testLocation.getY()>=5){
                testLocation.subtract(0,2,0);
            }
        }
        return testLocation;
    }

}
