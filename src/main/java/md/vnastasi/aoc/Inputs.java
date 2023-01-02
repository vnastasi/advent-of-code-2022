package md.vnastasi.aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Inputs {

    private Inputs() {
    }

    public static List<String> readLines(String fileName) {
        URL resource = Inputs.class.getClassLoader().getResource(fileName);
        try {
            return Files.readAllLines(Path.of(Objects.requireNonNull(resource).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readString(String fileName) {
        URL resource = Inputs.class.getClassLoader().getResource(fileName);
        try {
            return Files.readString(Path.of(Objects.requireNonNull(resource).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
