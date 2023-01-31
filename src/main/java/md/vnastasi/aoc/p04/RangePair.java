package md.vnastasi.aoc.p04;

record RangePair(Range one, Range two) {

    public boolean fullyOverlap() {
        return one.contains(two) || two.contains(one);
    }

    public boolean partiallyOverlap() {
        return one.overlapsWith(two) || two.overlapsWith(one);
    }

    public static RangePair fromString(String rangePairDefinition) {
        var rangeDefinitions = rangePairDefinition.split(",");
        return new RangePair(Range.fromString(rangeDefinitions[0]), Range.fromString(rangeDefinitions[1]));
    }
}
