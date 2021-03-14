package it.areson.interdimension.commands.tree;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class CommandParameterNode implements TreeNode, CommandExecutor {

    private TreeNode parent;
    private final String parameter;
    private final List<TreeNode> children;

    public CommandParameterNode(String parameter) {
        this.parent = null;
        this.parameter = parameter;
        this.children = new ArrayList<>();
    }

    @Override
    public Optional<TreeNode> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public boolean addChild(TreeNode commandExecutorNode) {
        if (!this.children.contains(commandExecutorNode)) {
            this.children.add(commandExecutorNode);
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return parameter;
    }

    @Override
    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public Optional<TreeNode> getChild(String key) {
        return children.stream().filter(commandExecutorNode -> Objects.equals(commandExecutorNode.getName(), key)).findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandParameterNode)) return false;
        CommandParameterNode node = (CommandParameterNode) o;
        return parameter.equals(node.parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameter);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public abstract boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments);
}
