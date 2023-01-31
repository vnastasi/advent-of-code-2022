package md.vnastasi.aoc.p03;

import md.vnastasi.aoc.util.Inputs;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Puzzle03 {

    public void run() {
        var lines = Inputs.readLines("input_03.txt");
        partOne(lines);
        partTwo(lines);
    }

    private void partOne(List<String> lines) {
        Function<String, Character> reductionFunction = (line) -> {
            var firstHalf = line.substring(0, line.length() / 2);
            var secondHalf = line.substring(line.length() / 2);

            return IntStream.range(0, firstHalf.length())
                    .mapToObj(firstHalf::charAt)
                    .filter(it -> secondHalf.contains(it.toString()))
                    .findFirst()
                    .orElse(null);
        };

        var prioritySum = lines.stream()
                .map(reductionFunction)
                .filter(Objects::nonNull)
                .mapToInt(this::priorityNumericValue)
                .sum();

        System.out.printf("Sum of priorities is %1d\n", prioritySum);
    }

    private void partTwo(List<String> lines) {
        Function<List<String>, Character> reductionFunction = (batch) -> {
            var firstLine = batch.get(0);
            var secondLine = batch.get(1);
            var thirdLine = batch.get(2);

            return IntStream.range(0, firstLine.length())
                    .mapToObj(firstLine::charAt)
                    .filter(it -> secondLine.contains(it.toString()) && thirdLine.contains(it.toString()))
                    .findFirst()
                    .orElse(null);
        };

        var prioritySum = IntStream.iterate(0, i -> i < lines.size(), i -> i + 3)
                .mapToObj(i -> lines.subList(i, Math.min(i + 3, lines.size())))
                .toList()
                .stream()
                .map(reductionFunction)
                .filter(Objects::nonNull)
                .mapToInt(this::priorityNumericValue)
                .sum();

        System.out.printf("Sum of priorities is %1d\n", prioritySum);
    }

    private int priorityNumericValue(Character character) {
        if (Character.isUpperCase(character)) {
            return character - 38;
        } else {
            return character - 96;
        }
    }

    public static void main(String[] args) {
        new Puzzle03().run();
    }
}
