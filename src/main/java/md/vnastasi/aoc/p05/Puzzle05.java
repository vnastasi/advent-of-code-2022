package md.vnastasi.aoc.p05;

import md.vnastasi.aoc.util.Inputs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static md.vnastasi.aoc.util.Collections.toReversedList;

public class Puzzle05 {

    public void run() {
        var lines = Inputs.readLines("input_05.txt");
        var instructions = lines.stream().skip(10).map(Instruction::fromLine).toList();
        partOne(createInitialStacks(), instructions);
        partTwo(createInitialStacks(), instructions);
    }

    private void partOne(List<Deque<String>> stacks, List<Instruction> instructions) {
        instructions.forEach(instruction -> {
            var fromStack = stacks.get(instruction.fromStack() - 1);
            var toStack = stacks.get(instruction.toStack() - 1);
            IntStream.range(0, instruction.quantity())
                    .forEach(i -> toStack.push(fromStack.pop()));
        });

        var message = stacks.stream().map(Deque::pop).collect(Collectors.joining());
        System.out.printf("Message for 9000 is %s\n", message);
    }

    private void partTwo(List<Deque<String>> stacks, List<Instruction> instructions) {
        instructions.forEach(instruction -> {
            var fromStack = stacks.get(instruction.fromStack() - 1);
            var toStack = stacks.get(instruction.toStack() - 1);
            IntStream.range(0, instruction.quantity())
                    .mapToObj(i -> fromStack.pop())
                    .collect(toReversedList())
                    .forEach(toStack::push);
        });

        var message = stacks.stream().map(Deque::pop).collect(Collectors.joining());
        System.out.printf("Message for 9001 is %s\n", message);
    }

    private List<Deque<String>> createInitialStacks() {
        return List.of(
                new ArrayDeque<>(List.of("N", "Q", "L", "S", "C", "Z", "P", "T")),
                new ArrayDeque<>(List.of("G", "C", "H", "V", "T", "P", "L")),
                new ArrayDeque<>(List.of("F", "Z", "C", "D")),
                new ArrayDeque<>(List.of("C", "V", "M", "L", "D", "T", "W", "G")),
                new ArrayDeque<>(List.of("C", "W", "P")),
                new ArrayDeque<>(List.of("Z", "S", "T", "C", "D", "J", "F", "P")),
                new ArrayDeque<>(List.of("D", "B", "G", "W", "V")),
                new ArrayDeque<>(List.of("W", "H", "Q", "S", "J", "N")),
                new ArrayDeque<>(List.of("V", "L", "S", "F", "Q", "C", "R"))
        );
    }

    public static void main(String[] args) {
        new Puzzle05().run();
    }
}
