package md.vnastasi.aoc.p11;

sealed interface ArithmeticNode {

    int compute();

    record Value(int value) implements ArithmeticNode {

        @Override
        public int compute() {
            return value;
        }
    }

    record Sum(ArithmeticNode left, ArithmeticNode right) implements ArithmeticNode {

        @Override
        public int compute() {
            return left.compute() + right.compute();
        }
    }

    record Product(ArithmeticNode left, ArithmeticNode right) implements ArithmeticNode {

        @Override
        public int compute() {
            return left.compute() * right.compute();
        }
    }
}
