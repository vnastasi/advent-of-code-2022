package md.vnastasi.aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class Puzzle03 {

    public void run() throws URISyntaxException, IOException {
        URL resource = Puzzle02.class.getClassLoader().getResource("input_03.txt");
        var lines = Files.readAllLines(Path.of(resource.toURI()));

        partOne(lines);
        partTwo(lines);
    }

    private void partOne(List<String> lines) {
        var prioritySum = lines.stream()
                .map(line -> {
                    var firstHalf = line.substring(0, line.length() / 2);
                    var secondHalf = line.substring(line.length() / 2);

                    return IntStream.range(0, firstHalf.length())
                            .mapToObj(firstHalf::charAt)
                            .filter(it -> secondHalf.contains("" + it))
                            .findFirst().orElse(null);
                })
                .filter(Objects::nonNull)
                .mapToInt(item -> {
                    if (Character.isUpperCase(item)) {
                        return item - 38;
                    } else {
                        return item - 96;
                    }
                })
                .sum();

        System.out.printf("Sum of priorities is %1d\n", prioritySum);
    }

    private void partTwo(List<String> lines) {
        var prioritySum = IntStream.iterate(0, i -> i < lines.size(), i -> i + 3)
                .mapToObj(i -> lines.subList(i, Math.min(i + 3, lines.size())))
                .toList()
                .stream()
                .map(batch -> {
                            var firstLine = batch.get(0);
                            var secondLine = batch.get(1);
                            var thirdLine = batch.get(2);

                            return IntStream.range(0, firstLine.length())
                                    .mapToObj(firstLine::charAt)
                                    .filter(it -> secondLine.contains("" + it) && thirdLine.contains("" + it))
                                    .findFirst().orElse(null);
                        }
                )
                .filter(Objects::nonNull)
                .mapToInt(item -> {
                    if (Character.isUpperCase(item)) {
                        return item - 38;
                    } else {
                        return item - 96;
                    }
                })
                .sum();

        System.out.printf("Sum of priorities is %1d\n", prioritySum);
    }

    public static void main(String[] args) {
        try {
            new Puzzle03().run();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
