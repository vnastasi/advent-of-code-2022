package md.vnastasi.aoc.p07;

import md.vnastasi.aoc.util.Inputs;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

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

    public static void main(String[] args) {
        new Puzzle07().run();
    }
}
