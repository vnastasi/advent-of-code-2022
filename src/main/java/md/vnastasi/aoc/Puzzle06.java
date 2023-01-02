package md.vnastasi.aoc;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle06 {

    public void run() {
        var input = Inputs.readString("input_06.txt");
        partOne(input);
        partTwo(input);
    }

    private void partOne(String input) {
        locateMarker(input, 4);
    }

    private void partTwo(String input) {
        locateMarker(input, 14);
    }

    private void locateMarker(String input, int markerSize) {
        var marker = new StringBuilder(input.substring(0, markerSize));
        for (var i = markerSize; i < input.length(); i++) {
            if (hasUniqueCharacters(marker)) {
                System.out.println("Result is " + i);
                break;
            }
            marker.deleteCharAt(0).append(input.charAt(i));
        }
    }

    private boolean hasUniqueCharacters(CharSequence s) {
        return IntStream.range(0, s.length()).mapToObj(s::charAt).collect(Collectors.toSet()).size() == s.length();
    }

    public static void main(String[] args) {
        new Puzzle06().run();
    }
}
