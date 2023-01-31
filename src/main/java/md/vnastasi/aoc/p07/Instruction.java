package md.vnastasi.aoc.p07;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

sealed interface Instruction {

    Pattern cdPattern = Pattern.compile("\\$ cd ([a-zA-Z]+)");
    Pattern dirPattern = Pattern.compile("dir (.+)");
    Pattern filePattern = Pattern.compile("(\\d+) (.+)");

    final class MoveToRoot implements Instruction {

        @Override
        public String toString() {
            return "$ cd /";
        }
    }

    record MoveToDirectory(String name) implements Instruction {

        @Override
        public String toString() {
            return "$ cd " + name;
        }
    }

    final class ListContents implements Instruction {

        @Override
        public String toString() {
            return "$ ls";
        }
    }

    sealed interface Found extends Instruction {

        record File(String name, long size) implements Found {

            @Override
            public String toString() {
                return size + " " + name;
            }
        }

        record Directory(String name) implements Found {

            @Override
            public String toString() {
                return "dir " + name;
            }
        }
    }

    final class MoveToParent implements Instruction {

        @Override
        public String toString() {
            return "$ cd ..";
        }
    }

    static @NotNull Instruction fromLine(@NotNull String line) {
        return switch (line) {
            case "$ cd /" -> new MoveToRoot();
            case "$ ls" -> new ListContents();
            case "$ cd .." -> new MoveToParent();
            default -> {
                var cdMatcher = Instruction.cdPattern.matcher(line);
                if (cdMatcher.matches()) {
                    yield new MoveToDirectory(cdMatcher.group(1));
                }
                var dirMatcher = dirPattern.matcher(line);
                if (dirMatcher.matches()) {
                    yield new Found.Directory(dirMatcher.group(1));
                }
                var fileMatcher = filePattern.matcher(line);
                if (fileMatcher.matches()) {
                    yield new Found.File(fileMatcher.group(2), Long.parseLong(fileMatcher.group(1)));
                }

                throw new IllegalArgumentException("Line doesn't match any instruction: " + line);
            }
        };
    }
}
