package it.areson.interdimension.commands.tree;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

@SuppressWarnings("NullableProblems")
public class CommandTree implements CommandExecutor, TabCompleter {

    private final CommandExecutorNode root;

    public CommandTree(CommandExecutorNode rootCommand) {
        this.root = rootCommand;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] arguments) {
        return null;
    }

}
