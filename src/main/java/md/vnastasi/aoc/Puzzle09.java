package md.vnastasi.aoc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.*;

public class Puzzle09 {

    public void run() {
        var lines = Inputs.readLines("input_09.txt");
        var instructions = lines.stream().map(Instruction::fromLine).toList();
        partOne(instructions);
        partTwo(instructions);
    }

    private void partOne(List<Instruction> instructions) {
        simulate(instructions, 2);
    }

    private void partTwo(List<Instruction> instructions) {
        simulate(instructions, 10);
    }

    private void simulate(List<Instruction> instructions, int numberOfKnots) {
        var tailIndex = numberOfKnots - 1;
        var knots = IntStream.range(0, numberOfKnots).mapToObj(it -> new Position(0, 0)).collect(Collectors.toList());
        var uniqueTailPositions = new HashSet<Position>();
        uniqueTailPositions.add(knots.get(tailIndex));

        for (var instruction : instructions) {
            for (var i = 0; i < instruction.distance; i++) {
                knots.set(0, knots.get(0).adjust(instruction.withDistance(1)));
                for (var k = 1; k < knots.size(); k++) {
                    knots.set(k, computeNewPosition(knots.get(k - 1), knots.get(k)));
                }
                uniqueTailPositions.add(knots.get(tailIndex));
            }
        }

        System.out.println("Number of unique tail positions (with " + numberOfKnots + " knots): " + uniqueTailPositions.size());
    }

    private Position computeNewPosition(Position frontKnotPosition, Position currentKnotPosition) {
        var distance = sqrt(pow(frontKnotPosition.x - currentKnotPosition.x, 2) + pow(frontKnotPosition.y - currentKnotPosition.y, 2));
        var dx = frontKnotPosition.x - currentKnotPosition.x;
        var dy = frontKnotPosition.y - currentKnotPosition.y;
        var moveX = 0;
        var moveY = 0;

        if (distance > 2.0) {
            moveX = (int) signum(dx);
            moveY = (int) signum(dy);
        } else if (distance == 2.0) {
            moveX = dx / 2;
            moveY = dy / 2;
        }

        return new Position(currentKnotPosition.x + moveX, currentKnotPosition.y + moveY);
    }

    private enum Move {

        UP("U"), DOWN("D"), LEFT("L"), RIGHT("R");

        private final String code;

        Move(String code) {
            this.code = code;
        }

        public static Move ofCode(String code) {
            return Arrays.stream(values()).filter(it -> it.code.equals(code)).findFirst().orElseThrow();
        }
    }

    private record Instruction(Move move, int distance) {

        public Instruction withDistance(int newDistance) {
            return new Instruction(move, newDistance);
        }

        @Override
        public String toString() {
            return move.code + " " + distance;
        }

        public static Instruction fromLine(String line) {
            var tokens = line.split(" ");
            return new Instruction(Move.ofCode(tokens[0]), Integer.parseInt(tokens[1]));
        }
    }

    private record Position(int x, int y) {

        public Position adjust(Instruction instruction) {
            var newX = x;
            var newY = y;
            switch (instruction.move) {
                case UP -> newY = y + instruction.distance;
                case DOWN -> newY = y - instruction.distance;
                case LEFT -> newX = x - instruction.distance;
                case RIGHT -> newX = x + instruction.distance;
            }
            return new Position(newX, newY);
        }

        @Override
        public String toString() {
            return String.format("(%d %d)", x, y);
        }
    }

    public static void main(String[] args) {
        new Puzzle09().run();
    }
}
