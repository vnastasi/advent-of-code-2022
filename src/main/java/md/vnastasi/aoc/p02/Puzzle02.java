package md.vnastasi.aoc.p02;

import md.vnastasi.aoc.util.Inputs;

import java.util.List;
import java.util.Map;

public class Puzzle02 {

    private void run() {
        var lines = Inputs.readLines("input_02.txt");
        partOne(lines);
        partTwo(lines);
    }

    private void partOne(List<String> lines) {
        var shapeMap = Map.of(
                "X", Shape.ROCK,
                "Y", Shape.PAPER,
                "Z", Shape.SCISSORS
        );

        var gameMap = Map.of(
                new Game("A", "X"), Outcome.DRAW,
                new Game("A", "Y"), Outcome.WIN,
                new Game("A", "Z"), Outcome.LOSE,
                new Game("B", "X"), Outcome.LOSE,
                new Game("B", "Y"), Outcome.DRAW,
                new Game("B", "Z"), Outcome.WIN,
                new Game("C", "X"), Outcome.WIN,
                new Game("C", "Y"), Outcome.LOSE,
                new Game("C", "Z"), Outcome.DRAW
        );

        var totalScore = lines.stream()
                .map(Game::fromString)
                .mapToInt(it -> gameMap.get(it).score + shapeMap.get(it.ownMove()).score)
                .sum();

        System.out.printf("Total score is %1d\n", totalScore);
    }

    private void partTwo(List<String> lines) {
        var gameMap = Map.of(
                "X", Outcome.LOSE,
                "Y", Outcome.DRAW,
                "Z", Outcome.WIN
        );

        var shapeMap = Map.of(
                new Game("A", "X"), Shape.SCISSORS,
                new Game("A", "Y"), Shape.ROCK,
                new Game("A", "Z"), Shape.PAPER,
                new Game("B", "X"), Shape.ROCK,
                new Game("B", "Y"), Shape.PAPER,
                new Game("B", "Z"), Shape.SCISSORS,
                new Game("C", "X"), Shape.PAPER,
                new Game("C", "Y"), Shape.SCISSORS,
                new Game("C", "Z"), Shape.ROCK
        );

        var totalScore = lines.stream()
                .map(Game::fromString)
                .mapToInt(it -> gameMap.get(it.ownMove()).score + shapeMap.get(it).score)
                .sum();

        System.out.printf("Total score is %1d\n", totalScore);
    }

    public static void main(String[] args) {
        new Puzzle02().run();
    }
}
