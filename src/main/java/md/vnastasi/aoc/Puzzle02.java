package md.vnastasi.aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Puzzle02 {

    private void run() throws URISyntaxException, IOException {
        URL resource = Puzzle02.class.getClassLoader().getResource("input_02.txt");
        var lines = Files.readAllLines(Path.of(resource.toURI()));

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
                .map(it -> it.split(" "))
                .map(it -> new Game(it[0], it[1]))
                .mapToInt(it -> gameMap.get(it).score + shapeMap.get(it.second()).score)
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
                .map(it -> it.split(" "))
                .map(it -> new Game(it[0], it[1]))
                .mapToInt(it -> gameMap.get(it.second()).score + shapeMap.get(it).score)
                .sum();

        System.out.printf("Total score is %1d\n", totalScore);
    }

    private enum Shape {

        ROCK(1), PAPER(2), SCISSORS(3);

        public final int score;

        Shape(int score) {
            this.score = score;
        }

    }

    private enum Outcome {

        WIN(6), DRAW(3), LOSE(0);

        public final int score;

        Outcome(int score) {
            this.score = score;
        }

    }

    private record Game(String first, String second) {
    }

    public static void main(String[] args) {
        try {
            new Puzzle02().run();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
