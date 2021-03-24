package it.areson.interdimension.commands.tree;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class CommandTree implements CommandExecutor, TabCompleter {

    private final TreeNode root;

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

    private int calculateHeightFromNode(TreeNode node) {
        if (node.isLeaf()) {
            return 0;
        }
        return Collections.max(node.getChildren().stream().map(treeNode -> 1 + calculateHeightFromNode(treeNode)).collect(Collectors.toList()));
    }

    public int treeHeight() {
        if (Objects.isNull(root)) {
            return 0;
        }
        return calculateHeightFromNode(root);
    }

}
