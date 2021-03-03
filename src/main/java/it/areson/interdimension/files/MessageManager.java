package it.areson.interdimension.files;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class MessageManager extends FileManager {

    private final String prefix;

    public MessageManager(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
        prefix = getFileConfiguration().getString("prefix", "");
    }

    public void sendMessage(boolean putPrefix, Player player, String messageKey) {
        String message = getFileConfiguration().getString(messageKey);
        if (Objects.nonNull(message)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', (putPrefix ? (prefix) : "") + message));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', (putPrefix ? prefix : "") + "&cError: '" + messageKey + "' message does not exists!"));
        }
    }

    @SafeVarargs
    public final void sendMessage(boolean putPrefix, Player player, String messageKey, Pair<String, String>... substitutions) {
        String message = getFileConfiguration().getString(messageKey);
        if (Objects.nonNull(message)) {
            for (Pair<String, String> stringPair : substitutions) {
                message = message.replaceAll(stringPair.left(), stringPair.right());
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', (putPrefix ? (prefix) : "") + message));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', (putPrefix ? prefix : "") + "&cError: '" + messageKey + "' message does not exists!"));
        }
    }

    @SafeVarargs
    public final String getMessage(boolean putPrefix, String messageKey, Pair<String, String>... substitutions) {
        String message = getFileConfiguration().getString(messageKey);
        if (Objects.nonNull(message)) {
            for (Pair<String, String> stringPair : substitutions) {
                message = message.replaceAll(stringPair.left(), stringPair.right());
            }
            return ChatColor.translateAlternateColorCodes('&', (putPrefix ? (prefix) : "") + message);
        } else {
            return ChatColor.translateAlternateColorCodes('&', (putPrefix ? prefix : "") + "&cError: '" + messageKey + "' message does not exists!");
        }
    }

    public String getMessage(boolean putPrefix, String messageKey) {
        String message = getFileConfiguration().getString(messageKey);
        if (Objects.nonNull(message)) {
            return ChatColor.translateAlternateColorCodes('&', (putPrefix ? (prefix) : "") + message);
        } else {
            return ChatColor.translateAlternateColorCodes('&', (putPrefix ? prefix : "") + "&cError: '" + messageKey + "' message does not exists!");
        }
    }

}
