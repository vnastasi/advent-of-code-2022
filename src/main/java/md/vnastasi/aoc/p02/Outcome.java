package md.vnastasi.aoc.p02;

enum Outcome {

    WIN(6), DRAW(3), LOSE(0);

    public final int score;

    Outcome(int score) {
        this.score = score;
    }
}
