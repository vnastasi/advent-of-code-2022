package md.vnastasi.aoc.p11;

import md.vnastasi.aoc.util.Inputs;

import java.util.Comparator;
import java.util.List;

public class Puzzle11 {

    public void run() {
        var lines = Inputs.readLines("input_11a.txt");
        var monkeys = new Parser().parse(lines);
        partOne(monkeys);
    }

    private void partOne(List<Monkey> monkeys) {
        for (var i = 0; i < 20; i++) {
            System.out.println("=====================================");
            System.out.println("Round " + (i + 1));
            for (var monkey : monkeys) {
                System.out.println("At beginning:");
                monkeys.forEach(System.out::println);
                var itemIterator = monkey.getItems().iterator();
                while (itemIterator.hasNext()) {
                    var item = itemIterator.next();
                    var newItem = (int) Math.floor(monkey.getTransformation().apply(item) / 3.0);
                    var destinationIndex = monkey.getDestinationChoice().get(monkey.getTest().test(newItem));
                    monkeys.stream().filter(it -> it.getIndex() == destinationIndex).findFirst().ifPresent(it -> it.getItems().add(newItem));
                    monkey.incrementNumberOfItemsInspected();
                    itemIterator.remove();
                }
                System.out.println("At end: ");
                monkeys.forEach(System.out::println);
            }
        }

        var result = monkeys.stream()
                .sorted(Comparator.comparingInt(Monkey::getNumberOfItemsInspected).reversed())
                .limit(2)
                .peek(System.out::println)
                .mapToInt(Monkey::getNumberOfItemsInspected)
                .reduce(1, (it, acc) -> acc * it);
        System.out.println("Most active 2 monkeys inspected " + result + " items");
    }

    public static void main(String[] args) {
        new Puzzle11().run();
    }
}
