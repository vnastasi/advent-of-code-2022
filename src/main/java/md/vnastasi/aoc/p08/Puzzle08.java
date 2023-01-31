package md.vnastasi.aoc.p08;

import md.vnastasi.aoc.util.Inputs;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.IntStream;

public class Puzzle08 {

    public void run() {
        var lines = Inputs.readLines("input_08.txt");
        var grid = createGrid(lines);
        partOne(grid);
        partTwo(grid);
    }

    private void partOne(int[][] grid) {
        Function<Point, IntPredicate> condition = controlPoint -> (it -> it < controlPoint.value());

        Predicate<Point> visibleFromTop = point -> IntStream.iterate(point.i() - 1, k -> k >= 0, k -> k - 1)
                .map(it -> grid[it][point.j()])
                .allMatch(condition.apply(point));

        Predicate<Point> visibleFromBottom = point -> IntStream.range(point.i() + 1, grid.length)
                .map(it -> grid[it][point.j()])
                .allMatch(condition.apply(point));

        Predicate<Point> visibleFromLeft = point -> IntStream.iterate(point.j() - 1, k -> k >= 0, k -> k - 1)
                .map(it -> grid[point.i()][it])
                .allMatch(condition.apply(point));

        Predicate<Point> visibleFromRight = point -> Arrays.stream(grid[point.i()], point.j() + 1, grid[point.i()].length)
                .allMatch(condition.apply(point));

        Predicate<Point> visibleFromAnyDirection = visibleFromTop.or(visibleFromBottom).or(visibleFromLeft).or(visibleFromRight);

        var counter = 0;
        for (var i = 1; i < grid.length - 1; i++) {
            for (var j = 1; j < grid[i].length - 1; j++) {
                var point = new Point(i, j, grid[i][j]);
                if (visibleFromAnyDirection.test(point)) {
                    counter++;
                }
            }
        }
        var total = counter + 2 * grid.length + 2 * (grid[0].length - 2);
        System.out.println("Total visible trees: " + total);
    }

    public void partTwo(int[][] grid) {
        Function<Point, IntPredicate> condition = controlPoint -> (it -> it < controlPoint.value());

        ToLongFunction<Point> top = point -> IntStream.iterate(point.i() - 1, k -> k > 0, k -> k - 1)
                .map(it -> grid[it][point.j()])
                .takeWhile(condition.apply(point))
                .count() + 1;

        ToLongFunction<Point> bottom = point -> IntStream.range(point.i() + 1, grid.length - 1)
                .map(it -> grid[it][point.j()])
                .takeWhile(condition.apply(point))
                .count() + 1;

        ToLongFunction<Point> left = point -> IntStream.iterate(point.j() - 1, k -> k > 0, k -> k - 1)
                .map(it -> grid[point.i()][it])
                .takeWhile(condition.apply(point))
                .count() + 1;

        ToLongFunction<Point> right = point -> Arrays.stream(grid[point.i()], point.j() + 1, grid[point.i()].length - 1)
                .takeWhile(condition.apply(point))
                .count() + 1;

        var highestScore = 0L;
        for (var i = 1; i < grid.length - 1; i++) {
            for (var j = 1; j < grid[i].length - 1; j++) {
                var point = new Point(i, j, grid[i][j]);
                var totalScore = top.applyAsLong(point) * bottom.applyAsLong(point) * right.applyAsLong(point) * left.applyAsLong(point);
                if (totalScore > highestScore) {
                    highestScore = totalScore;
                }
            }
        }
        System.out.println("Highest scenic score is: " + highestScore);
    }

    private int[][] createGrid(List<String> lines) {
        var grid = new int[lines.size()][];
        for (var i = 0; i < grid.length; i++) {
            var row = lines.get(i).toCharArray();
            grid[i] = new int[row.length];
            for (int j = 0; j < row.length; j++) {
                grid[i][j] = Character.getNumericValue(row[j]);
            }
        }
        return grid;
    }

    public static void main(String[] args) {
        new Puzzle08().run();
    }
}
