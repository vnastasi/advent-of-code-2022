package md.vnastasi.aoc.p09;

import java.util.Arrays;

enum Move {

    UP("U"), DOWN("D"), LEFT("L"), RIGHT("R");

    public final String code;

    Move(String code) {
        this.code = code;
    }

    public static Move ofCode(String code) {
        return Arrays.stream(values()).filter(it -> it.code.equals(code)).findFirst().orElseThrow();
    }
}
