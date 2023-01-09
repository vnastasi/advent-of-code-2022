package md.vnastasi.aoc;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Puzzle11 {

    public void run() {
        var lines = Inputs.readLines("input_11a.txt");
        var monkeys = createMonkeyList(lines);
        partOne(monkeys);
    }

    private void partOne(List<Monkey> monkeys) {
        for (var i = 0; i < 20; i++) {
            System.out.println("=====================================");
            System.out.println("Round " + (i + 1));
            for (var monkey : monkeys) {
                System.out.println("At beginning:");
                monkeys.forEach(System.out::println);
                var itemIterator = monkey.items.iterator();
                while (itemIterator.hasNext()) {
                    var item = itemIterator.next();
                    var newItem = (int) Math.floor(monkey.transformation.apply(item) / 3.0);
                    var destinationIndex = monkey.destinationChoice.get(monkey.test.test(newItem));
                    monkeys.stream().filter(it -> it.index == destinationIndex).findFirst().ifPresent(it -> it.items.add(newItem));
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

    private List<Monkey> createMonkeyList(List<String> lines) {
        return new Parser().parse(lines);
    }

    private static final class Monkey {

        private final int index;
        private final List<Integer> items;
        private final Function<Integer, Integer> transformation;
        private final Predicate<Integer> test;
        private final Map<Boolean, Integer> destinationChoice;
        private int numberOfItemsInspected = 0;

        private Monkey(
                int index,
                List<Integer> items,
                Function<Integer, Integer> transformation,
                Predicate<Integer> test,
                Map<Boolean, Integer> destinationChoice
        ) {
            this.index = index;
            this.items = items;
            this.transformation = transformation;
            this.test = test;
            this.destinationChoice = destinationChoice;
        }

        public int getNumberOfItemsInspected() {
            return numberOfItemsInspected;
        }

        public void incrementNumberOfItemsInspected() {
            numberOfItemsInspected++;
        }

        @Override
        public String toString() {
            return "Monkey{" +
                    "index=" + index +
                    ", items=" + items +
                    ", numberOfItemsInspected=" + numberOfItemsInspected +
                    '}';
        }

        public static class Builder {

            private int index;
            private List<Integer> items;
            private Function<Integer, Integer> transformation;
            private Predicate<Integer> test;
            private final Map<Boolean, Integer> destinationChoice = new HashMap<>();

            public void setIndex(int index) {
                this.index = index;
            }

            public void setItems(List<Integer> items) {
                this.items = new ArrayList<>(items);
            }

            public void setTransformation(Function<Integer, Integer> transformation) {
                this.transformation = transformation;
            }

            public void setTest(Predicate<Integer> test) {
                this.test = test;
            }

            public void setTrueDestinationChoice(int destination) {
                destinationChoice.put(true, destination);
            }

            public void setFalseDestinationChoice(int destination) {
                destinationChoice.put(false, destination);
            }

            public Monkey build() {
                return new Monkey(index, items, transformation, test, destinationChoice);
            }
        }
    }

    private static class Parser {

        private static final Pattern INDEX_PATTERN = Pattern.compile("Monkey (\\d):");
        private static final Pattern STARTING_ITEMS_PATTERN = Pattern.compile("\\s{2}Starting\\sitems:\\s(.+)");
        private static final Pattern OPERATION_PATTERN = Pattern.compile("\\s{2}Operation:\\s(.+)");
        private static final Pattern TEST_PATTERN = Pattern.compile("\\s{2}Test:\\sdivisible\\sby\\s(\\d+)");
        private static final Pattern CHOICE_TRUE_PATTERN = Pattern.compile("\\s{4}If\\strue:\\sthrow\\sto\\smonkey\\s(\\d+)");
        private static final Pattern CHOICE_FALSE_PATTERN = Pattern.compile("\\s{4}If\\sfalse:\\sthrow\\sto\\smonkey\\s(\\d+)");


        public List<Monkey> parse(List<String> lines) {
            var monkeys = new ArrayList<Monkey>();
            var currentBuilder = new Monkey.Builder();
            for (var line : lines) {
                if (line.isEmpty()) {
                    monkeys.add(currentBuilder.build());
                    currentBuilder = new Monkey.Builder();
                    continue;
                }

                var indexMatcher = INDEX_PATTERN.matcher(line);
                if (indexMatcher.matches()) {
                    currentBuilder.setIndex(Integer.parseInt(indexMatcher.group(1)));
                    continue;
                }

                var startingItemsMatcher = STARTING_ITEMS_PATTERN.matcher(line);
                if (startingItemsMatcher.matches()) {
                    var listGroup = startingItemsMatcher.group(1);
                    var items = Arrays.stream(listGroup.split(", ")).map(Integer::parseInt).toList();
                    currentBuilder.setItems(items);
                    continue;
                }

                var operationMatcher = OPERATION_PATTERN.matcher(line);
                if (operationMatcher.matches()) {
                    var formula = operationMatcher.group(1);
                    formula = formula.substring(formula.indexOf('=') + 1).trim();
                    var tokens = formula.split(" ");
                    Function<Integer, Integer> transformation = new ArithmeticFunction(tokens[0], tokens[1], tokens[2]);
                    currentBuilder.setTransformation(transformation);
                    continue;
                }

                var testMatcher = TEST_PATTERN.matcher(line);
                if (testMatcher.matches()) {
                    var number = Integer.parseInt(testMatcher.group(1));
                    currentBuilder.setTest(it -> it % number == 0);
                    continue;
                }

                var trueChoiceMatcher = CHOICE_TRUE_PATTERN.matcher(line);
                if (trueChoiceMatcher.matches()) {
                    var destination = Integer.parseInt(trueChoiceMatcher.group(1));
                    currentBuilder.setTrueDestinationChoice(destination);
                    continue;
                }

                var falseChoiceMatcher = CHOICE_FALSE_PATTERN.matcher(line);
                if (falseChoiceMatcher.matches()) {
                    var destination = Integer.parseInt(falseChoiceMatcher.group(1));
                    currentBuilder.setFalseDestinationChoice(destination);
                }
            }

            if (!lines.isEmpty()) {
                monkeys.add(currentBuilder.build());
            }

            return monkeys;
        }
    }

    private sealed interface ArithmeticNode {

        int compute();

        record Value(int value) implements ArithmeticNode {

            @Override
            public int compute() {
                return value;
            }
        }

        record Sum(ArithmeticNode left, ArithmeticNode right) implements ArithmeticNode {

            @Override
            public int compute() {
                return left.compute() + right.compute();
            }
        }

        record Product(ArithmeticNode left, ArithmeticNode right) implements ArithmeticNode {

            @Override
            public int compute() {
                return left.compute() * right.compute();
            }
        }
    }

    private record ArithmeticFunction(
            String left,
            String operation,
            String right
    ) implements Function<Integer, Integer> {

        @Override
        public Integer apply(Integer integer) {
            var leftNode = createOperandNode(left, integer);
            var rightNode = createOperandNode(right, integer);
            var operationNode = createOperationNode(leftNode, rightNode);
            return operationNode.compute();
        }

        private ArithmeticNode createOperandNode(String operand, Integer variableSubstitution) {
            if (operand.equals("old")) {
                return new ArithmeticNode.Value(variableSubstitution);
            } else if (operand.matches("\\d+")) {
                return new ArithmeticNode.Value(Integer.parseInt(operand));
            } else {
                throw new IllegalArgumentException("Cannot create arithmetic node from " + operand);
            }
        }

        private ArithmeticNode createOperationNode(ArithmeticNode leftNode, ArithmeticNode rightNode) {
            return switch (operation) {
                case "+" -> new ArithmeticNode.Sum(leftNode, rightNode);
                case "*" -> new ArithmeticNode.Product(leftNode, rightNode);
                default -> throw new IllegalArgumentException("Cannot create arithmetic operation from " + operation);
            };
        }
    }

    public static void main(String[] args) {
        new Puzzle11().run();
    }
}
