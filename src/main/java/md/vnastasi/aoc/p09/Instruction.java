package md.vnastasi.aoc.p09;

record Instruction(Move move, int distance) {

    public Instruction withDistance(int newDistance) {
        return new Instruction(move, newDistance);
    }

    @Override
    public String toString() {
        return move.code + " " + distance;
    }

    public static Instruction fromLine(String line) {
        var tokens = line.split(" ");
        return new Instruction(Move.ofCode(tokens[0]), Integer.parseInt(tokens[1]));
    }
}
