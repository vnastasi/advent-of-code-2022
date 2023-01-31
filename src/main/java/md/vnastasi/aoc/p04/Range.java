package md.vnastasi.aoc.p04;

record Range(int start, int end) {

    public boolean contains(Range other) {
        return other.start >= start && other.end <= end;
    }

    public boolean overlapsWith(Range other) {
        return other.start >= start && other.start <= end;
    }

    public static Range fromString(String stringDefinition) {
        var edges = stringDefinition.split("-");
        return new Range(Integer.parseInt(edges[0]), Integer.parseInt(edges[1]));
    }
}
