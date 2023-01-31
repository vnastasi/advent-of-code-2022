package md.vnastasi.aoc.p09;

record Position(int x, int y) {

    public Position adjust(Instruction instruction) {
        var newX = x;
        var newY = y;
        switch (instruction.move()) {
            case UP -> newY = y + instruction.distance();
            case DOWN -> newY = y - instruction.distance();
            case LEFT -> newX = x - instruction.distance();
            case RIGHT -> newX = x + instruction.distance();
        }
        return new Position(newX, newY);
    }

    @Override
    public String toString() {
        return String.format("(%d %d)", x, y);
    }
}
