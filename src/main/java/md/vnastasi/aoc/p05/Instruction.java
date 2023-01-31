package md.vnastasi.aoc.p05;

import java.util.regex.Pattern;

record Instruction(int quantity, int fromStack, int toStack) {

    private static final Pattern PATTERN = Pattern.compile("\\d+");

    public static Instruction fromLine(String line) {
        var matcher = PATTERN.matcher(line);
        matcher.find();
        var quantity = Integer.parseInt(matcher.group());
        matcher.find();
        var fromStack = Integer.parseInt(matcher.group());
        matcher.find();
        var toStack = Integer.parseInt(matcher.group());
        return new Instruction(quantity, fromStack, toStack);
    }
}
