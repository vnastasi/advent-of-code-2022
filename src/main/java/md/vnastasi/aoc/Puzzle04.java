package md.vnastasi.aoc;

import java.util.List;

public class Puzzle04 {

    public void run() {
        var lines = Inputs.readLines("input_04.txt");
        partOne(lines);
        partTwo(lines);
    }

    private void partOne(List<String> lines) {
        var count = lines.stream()
                .map(RangePair::fromString)
                .filter(RangePair::fullyOverlap)
                .count();
        System.out.printf("There are %1d full overlapping pairs\n", count);
    }

    private void partTwo(List<String> lines) {
        long count = lines.stream()
                .map(RangePair::fromString)
                .filter(RangePair::partiallyOverlap)
                .count();
        System.out.printf("There are %1d any overlapping pairs\n", count);
    }

    private record RangePair(Range one, Range two) {

        public boolean fullyOverlap() {
            return one.contains(two) || two.contains(one);
        }

        public boolean partiallyOverlap() {
            return one.overlapsWith(two) || two.overlapsWith(one);
        }

        public static RangePair fromString(String rangePairDefinition) {
            var rangeDefinitions = rangePairDefinition.split(",");
            return new RangePair(Range.fromString(rangeDefinitions[0]), Range.fromString(rangeDefinitions[1]));
        }
    }

    private record Range(int start, int end) {

        public boolean contains(Range other) {
            return other.start >= start && other.end <= end;
        }

        public boolean overlapsWith(Range other) {
            return other.start >= start && other.start <= end;
        }

        public static Range fromString(String stringDefinition) {
            var edges = stringDefinition.split("-");
            return new Range(Integer.parseInt(edges[0]), Integer.parseInt(edges[1]));
        }
    }

    public static void main(String[] args) {
        new Puzzle04().run();
    }
}
