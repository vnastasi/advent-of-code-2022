package md.vnastasi.aoc.p01;

import md.vnastasi.aoc.util.Inputs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Puzzle01 {

    public void run() {
        var lines = Inputs.readLines("input_01.txt");
        var map = createMap(lines);
        partOne(map);
        partTwo(map);
    }

    private Map<Integer, Long> createMap(List<String> lines) {
        var currentElfNumber = new AtomicInteger(1);
        var map = new HashMap<Integer, Long>();
        map.put(currentElfNumber.get(), 0L);

        lines.forEach(line -> {
            if (line.isEmpty()) {
                map.put(currentElfNumber.incrementAndGet(), 0L);
            } else {
                map.computeIfPresent(currentElfNumber.get(), (key, oldValue) -> oldValue + Long.parseLong(line));
            }
        });

        return map;
    }

    private void partOne(Map<Integer, Long> map) {
        var topElf = map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        System.out.printf("Elf #%1d is carrying %2d calories\n", topElf.getKey(), topElf.getValue());
    }

    private void partTwo(Map<Integer, Long> map) {
        var top3TotalCalories = map.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(3L)
                .mapToLong(Map.Entry::getValue)
                .sum();

        System.out.printf("Top 3 elves are carrying a total of %1d calories\n", top3TotalCalories);
    }

    public static void main(String[] args) {
        new Puzzle01().run();
    }
}
