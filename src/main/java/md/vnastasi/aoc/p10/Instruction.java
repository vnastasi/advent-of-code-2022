package md.vnastasi.aoc.p10;

import java.util.function.Function;

sealed interface Instruction {

    int numberOfCycles();

    Function<Integer, Integer> operation();

    final class Noop implements Instruction {

        @Override
        public int numberOfCycles() {
            return 1;
        }

        @Override
        public Function<Integer, Integer> operation() {
            return Function.identity();
        }

        @Override
        public String toString() {
            return "noop";
        }
    }

    record Add(int value) implements Instruction {

        @Override
        public int numberOfCycles() {
            return 2;
        }

        @Override
        public Function<Integer, Integer> operation() {
            return it -> it + value;
        }

        @Override
        public String toString() {
            return "addx " + value;
        }
    }

    static Instruction fromLine(String line) {
        if (line.equals("noop")) {
            return new Noop();
        } else if (line.startsWith("addx")) {
            var tokens = line.split(" ");
            return new Add(Integer.parseInt(tokens[1]));
        } else {
            throw new IllegalArgumentException("Unknown instruction " + line);
        }
    }
}
