package md.vnastasi.aoc.p10;

import md.vnastasi.aoc.util.Inputs;

import java.util.List;
import java.util.Set;

public class Puzzle10 {

    private static final Set<Integer> CYCLE_CHECK_POINTS = Set.of(20, 60, 100, 140, 180, 220);

    public void run() {
        var lines = Inputs.readLines("input_10.txt");
        var instructions = lines.stream().map(Instruction::fromLine).toList();
        partOne(instructions);
        partTwo(instructions);
    }

    private void partOne(List<Instruction> instructions) {
        var currentCycle = 0;
        var registerValue = 1;
        var totalSignalStrength = 0;

        for (var instruction : instructions) {
            for (var i = 0; i < instruction.numberOfCycles(); i++) {
                currentCycle++;
                totalSignalStrength += computeSignalStrength(currentCycle, registerValue);
            }
            registerValue = instruction.operation().apply(registerValue);
        }

        System.out.println("Total signal strength: " + totalSignalStrength);
    }

    private void partTwo(List<Instruction> instructions) {
        var currentCycle = 0;
        var registerValue = 1;
        var row = new StringBuilder();

        for (var instruction : instructions) {
            for (var i = 0; i < instruction.numberOfCycles(); i++) {
                currentCycle++;
                drawPixel(row, currentCycle, registerValue);
                flushRow(row, currentCycle);
            }
            registerValue = instruction.operation().apply(registerValue);
        }
    }

    private int computeSignalStrength(int currentCycle, int registerValue) {
        if (CYCLE_CHECK_POINTS.contains(currentCycle)) {
            return currentCycle * registerValue;
        } else {
            return 0;
        }
    }

    private void drawPixel(StringBuilder row, int currentCycle, int registerValue) {
        int pixelPosition = currentCycle % 40 - 1;
        if (pixelPosition >= registerValue - 1 && pixelPosition <= registerValue + 1) {
            row.append("#");
        } else {
            row.append(".");
        }
    }

    private void flushRow(StringBuilder row, int currentCycle) {
        if (currentCycle % 40 == 0) {
            System.out.println(row);
            row.setLength(0);
        }
    }

    public static void main(String[] args) {
        new Puzzle10().run();
    }
}
