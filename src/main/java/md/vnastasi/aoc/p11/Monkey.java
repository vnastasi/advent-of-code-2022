package md.vnastasi.aoc.p11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

final class Monkey {

    private final int index;
    private final List<Integer> items;
    private final Function<Integer, Integer> transformation;
    private final Predicate<Integer> test;
    private final Map<Boolean, Integer> destinationChoice;
    private int numberOfItemsInspected = 0;

    private Monkey(
            int index,
            List<Integer> items,
            Function<Integer, Integer> transformation,
            Predicate<Integer> test,
            Map<Boolean, Integer> destinationChoice
    ) {
        this.index = index;
        this.items = items;
        this.transformation = transformation;
        this.test = test;
        this.destinationChoice = destinationChoice;
    }

    public int getIndex() {
        return index;
    }

    public List<Integer> getItems() {
        return items;
    }

    public Function<Integer, Integer> getTransformation() {
        return transformation;
    }

    public Predicate<Integer> getTest() {
        return test;
    }

    public Map<Boolean, Integer> getDestinationChoice() {
        return destinationChoice;
    }

    public int getNumberOfItemsInspected() {
        return numberOfItemsInspected;
    }

    public void incrementNumberOfItemsInspected() {
        numberOfItemsInspected++;
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "index=" + index +
                ", items=" + items +
                ", numberOfItemsInspected=" + numberOfItemsInspected +
                '}';
    }

    public static class Builder {

        private int index;
        private List<Integer> items;
        private Function<Integer, Integer> transformation;
        private Predicate<Integer> test;
        private final Map<Boolean, Integer> destinationChoice = new HashMap<>();

        public void setIndex(int index) {
            this.index = index;
        }

        public void setItems(List<Integer> items) {
            this.items = new ArrayList<>(items);
        }

        public void setTransformation(Function<Integer, Integer> transformation) {
            this.transformation = transformation;
        }

        public void setTest(Predicate<Integer> test) {
            this.test = test;
        }

        public void setTrueDestinationChoice(int destination) {
            destinationChoice.put(true, destination);
        }

        public void setFalseDestinationChoice(int destination) {
            destinationChoice.put(false, destination);
        }

        public Monkey build() {
            return new Monkey(index, items, transformation, test, destinationChoice);
        }
    }
}
