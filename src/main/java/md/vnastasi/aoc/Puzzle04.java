package md.vnastasi.aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Puzzle04 {

    public void run() throws URISyntaxException, IOException {
        URL resource = Puzzle02.class.getClassLoader().getResource("input_04.txt");
        var lines = Files.readAllLines(Path.of(resource.toURI()));

        partOne(lines);
    }

    private void partOne(List<String> lines) {
        var count = lines.stream()
                .map(line -> {
                    var rangeDefinitions = line.split(",");
                    return new RangePair(Range.of(rangeDefinitions[0]), Range.of(rangeDefinitions[1]));
                })
                .filter(it -> it.one.contains(it.two) || it.two.contains(it.one))
                .count();
        System.out.printf("There are %1d full overlaps\n", count);
    }

    public static void main(String[] args) {
        try {
            new Puzzle04().run();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private record Range(int start, int end) {

        public boolean contains(Range other) {
            return other.start >= start && other.end <= end;
        }

        public static Range of(String stringDefinition) {
            var edges = stringDefinition.split("-");
            return new Range(Integer.parseInt(edges[0]), Integer.parseInt(edges[1]));
        }
    }

    private record RangePair(Range one, Range two) {
    }
}
