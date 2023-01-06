package md.vnastasi.aoc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class Puzzle07 {

    public static final long FS_SIZE = 70_000_000L;
    public static final long UPDATE_SIZE = 30_000_000L;

    public void run() {
        var lines = Inputs.readLines("input_07.txt");
        var instructions = lines.stream().map(Instruction::fromLine).toList();
        var rootNode = createFileSystem(instructions);
        partOne(rootNode);
        partTwo(rootNode);
    }

    private void partOne(@NotNull Node rootNode) {
        var accumulator = new AtomicLong(0L);
        sum(rootNode, accumulator);
        System.out.println("Result is: " + accumulator.get());
    }

    private void partTwo(@NotNull Node rootNode) {
        var availableSpaceSize = FS_SIZE - rootNode.getSize();
        var targetSize = UPDATE_SIZE - availableSpaceSize;
        var targetNode = new AtomicReference<>(rootNode);
        search(rootNode, targetNode, targetSize);
        System.out.printf("Smallest directory to delete is %s with size of %d", targetNode.get().getName(), targetNode.get().getSize());
    }

    private void sum(@NotNull Node currentNode, AtomicLong accumulator) {
        if (currentNode.isDirectory() && currentNode.getSize() < 100_000L) {
            accumulator.addAndGet(currentNode.getSize());
        }
        if (currentNode.getChildren() == null || currentNode.getChildren().isEmpty()) {
            return;
        }
        currentNode.getChildren().forEach(it -> sum(it, accumulator));
    }

    private void search(@NotNull Node currentNode, AtomicReference<Node> targetNode, long targetSize) {
        if (currentNode.getSize() >= targetSize && currentNode.getSize() < targetNode.get().getSize()) {
            targetNode.set(currentNode);
        }
        if (currentNode.getChildren() == null || currentNode.getChildren().isEmpty()) {
            return;
        }
        currentNode.getChildren().forEach(it -> search(it, targetNode, targetSize));
    }

    private @NotNull Node createFileSystem(@NotNull List<Instruction> instructions) {
        Node currentNode = Node.nil();
        for (var instruction : instructions) {
            switch (instruction) {
                case Instruction.MoveToRoot mtr -> currentNode = Node.root();
                case Instruction.MoveToDirectory mtd -> currentNode = currentNode.findDirectory(mtd.name());
                case Instruction.MoveToParent mtp -> currentNode = currentNode.getParent();
                case Instruction.Found.Directory dir -> currentNode.addChildNode(Node.directory(dir.name()));
                case Instruction.Found.File file -> currentNode.addChildNode(Node.file(file.name(), file.size()));
                case Instruction.ListContents lc, null -> {}
            }
        }

        Node root = currentNode.getRoot();
        root.computeSize();
        return root;
    }


    private static class Node {

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

    private sealed interface Instruction {

        Pattern cdPattern = Pattern.compile("\\$ cd ([a-zA-Z]+)");
        Pattern dirPattern = Pattern.compile("dir (.+)");
        Pattern filePattern = Pattern.compile("(\\d+) (.+)");

        final class MoveToRoot implements Instruction {

            @Override
            public String toString() {
                return "$ cd /";
            }
        }

        record MoveToDirectory(String name) implements Instruction {

            @Override
            public String toString() {
                return "$ cd " + name;
            }
        }

        final class ListContents implements Instruction {

            @Override
            public String toString() {
                return "$ ls";
            }
        }

        sealed interface Found extends Instruction {

            record File(String name, long size) implements Found {

                @Override
                public String toString() {
                    return size + " " + name;
                }
            }

            record Directory(String name) implements Found {

                @Override
                public String toString() {
                    return "dir " + name;
                }
            }
        }

        final class MoveToParent implements Instruction {

            @Override
            public String toString() {
                return "$ cd ..";
            }
        }

        static @NotNull Instruction fromLine(@NotNull String line) {
            return switch (line) {
                case "$ cd /" -> new MoveToRoot();
                case "$ ls" -> new ListContents();
                case "$ cd .." -> new MoveToParent();
                default -> {
                    var cdMatcher = Instruction.cdPattern.matcher(line);
                    if (cdMatcher.matches()) {
                        yield new MoveToDirectory(cdMatcher.group(1));
                    }
                    var dirMatcher = dirPattern.matcher(line);
                    if (dirMatcher.matches()) {
                        yield new Found.Directory(dirMatcher.group(1));
                    }
                    var fileMatcher = filePattern.matcher(line);
                    if (fileMatcher.matches()) {
                        yield new Found.File(fileMatcher.group(2), Long.parseLong(fileMatcher.group(1)));
                    }

                    throw new IllegalArgumentException("Line doesn't match any instruction: " + line);
                }
            };
        }
    }

    public static void main(String[] args) {
        new Puzzle07().run();
    }
}
