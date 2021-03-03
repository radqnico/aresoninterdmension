package it.areson.interdimension;

import it.areson.interdimension.files.MessageManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AresonInterdimension extends JavaPlugin {

    private static AresonInterdimension instance;
    private MessageManager messages;

    public static AresonInterdimension getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        messages = new MessageManager(this, "messages.yml");
    }

    public MessageManager messages() {
        return messages;
    }
}
