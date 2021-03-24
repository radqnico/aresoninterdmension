package it.areson.interdimension.commands.tree;

import java.util.List;
import java.util.Optional;

public interface TreeNode {

    Optional<? extends TreeNode> getParent();

    String getName();

    List<? extends TreeNode> getChildren();

    Optional<? extends TreeNode> getChild(String key);

    boolean isLeaf();

}
