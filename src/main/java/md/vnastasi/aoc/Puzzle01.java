package md.vnastasi.aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.interfaces.ECKey;
import java.util.*;

public class Puzzle01 {

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        var scanner = new Scanner(Puzzle01.class.getClassLoader().getResourceAsStream("input_01.txt"));

        var currentElfNumber = 1;
        var map = new HashMap<Integer, Long>();
        map.put(currentElfNumber, 0L);

        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            if (line.isEmpty()) {
                currentElfNumber++;
                map.put(currentElfNumber, 0L);
                continue;
            }

            var caloriesValue = Long.parseLong(line);
            map.computeIfPresent(currentElfNumber, (key, oldValue) -> oldValue + caloriesValue);
        }

        var topElf = map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();
        System.out.printf("Elf #%1d is carrying %2d calories\n", topElf.getKey(), topElf.getValue());

        var top3TotalCalories = map.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(3L)
                .mapToLong(Map.Entry::getValue)
                .sum();
        System.out.printf("Top 3 elf are carrying a total of %1d calories\n", top3TotalCalories);
    }
}
