package md.vnastasi.aoc.p02;

enum Shape {

    ROCK(1), PAPER(2), SCISSORS(3);

    public final int score;

    Shape(int score) {
        this.score = score;
    }
}
