package md.vnastasi.aoc.p02;

record Game(String opponentMove, String ownMove) {

    public static Game fromString(String gameDefinition) {
        var moves = gameDefinition.split(" ");
        return new Game(moves[0], moves[1]);
    }
}
