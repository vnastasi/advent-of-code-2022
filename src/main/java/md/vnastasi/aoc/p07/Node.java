package md.vnastasi.aoc.p07;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

class Node {

    private final @NotNull String name;
    private final @Nullable Collection<Node> children;
    private @Nullable Node parent;
    private long size;

    private Node(@NotNull String name, long size, @Nullable Collection<Node> children) {
        this.name = name;
        this.size = size;
        this.children = children;
    }

    public @NotNull String getName() {
        return name;
    }

    public @Nullable Node getParent() {
        return parent;
    }

    public long getSize() {
        return size;
    }

    public @Nullable Collection<Node> getChildren() {
        return children;
    }

    public void addChildNode(@NotNull Node child) {
        child.parent = this;
        if (children != null) {
            children.add(child);
        }
    }

    public boolean isDirectory() {
        return children != null;
    }

    public @NotNull Node findDirectory(String name) {
        if (children == null || children.isEmpty()) {
            throw new IllegalStateException(this.name + " has no children");
        }
        return children.stream()
                .filter(it -> it.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(this.name + " has no child directory named " + name));
    }

    public @NotNull Node getRoot() {
        Node currentNode = this;
        while (currentNode.parent != null) {
            currentNode = currentNode.parent;
        }
        return currentNode;
    }

    public void computeSize() {
        if (children != null && !children.isEmpty()) {
            children.forEach(Node::computeSize);
            this.size = children.stream().mapToLong(Node::getSize).sum();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public static @NotNull Node root() {
        return new Node("/", 0, new ArrayList<>());
    }

    public static @NotNull Node directory(String name) {
        return new Node(name, 0, new ArrayList<>());
    }

    public static @NotNull Node file(String name, long size) {
        return new Node(name, size, null);
    }

    public static @NotNull Node nil() {
        return new Node("", 0L, null);
    }
}
