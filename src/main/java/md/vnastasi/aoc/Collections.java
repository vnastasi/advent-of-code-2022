package md.vnastasi.aoc;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Collections {

    private Collections() {
    }

    public static <T> Collector<T, ?, List<T>> toReversedList() {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            java.util.Collections.reverse(list);
            return list;
        });
    }
}
