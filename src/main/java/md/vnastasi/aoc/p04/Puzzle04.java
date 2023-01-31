package md.vnastasi.aoc.p04;

import md.vnastasi.aoc.util.Inputs;

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

    public static void main(String[] args) {
        new Puzzle04().run();
    }
}
