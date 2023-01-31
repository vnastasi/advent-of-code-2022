package md.vnastasi.aoc.p11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

class Parser {

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
