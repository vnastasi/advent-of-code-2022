package md.vnastasi.aoc.p11;

import java.util.function.Function;

record ArithmeticFunction(
        String left,
        String operation,
        String right
) implements Function<Integer, Integer> {

    @Override
    public Integer apply(Integer integer) {
        var leftNode = createOperandNode(left, integer);
        var rightNode = createOperandNode(right, integer);
        var operationNode = createOperationNode(leftNode, rightNode);
        return operationNode.compute();
    }

    private ArithmeticNode createOperandNode(String operand, Integer variableSubstitution) {
        if (operand.equals("old")) {
            return new ArithmeticNode.Value(variableSubstitution);
        } else if (operand.matches("\\d+")) {
            return new ArithmeticNode.Value(Integer.parseInt(operand));
        } else {
            throw new IllegalArgumentException("Cannot create arithmetic node from " + operand);
        }
    }

    private ArithmeticNode createOperationNode(ArithmeticNode leftNode, ArithmeticNode rightNode) {
        return switch (operation) {
            case "+" -> new ArithmeticNode.Sum(leftNode, rightNode);
            case "*" -> new ArithmeticNode.Product(leftNode, rightNode);
            default -> throw new IllegalArgumentException("Cannot create arithmetic operation from " + operation);
        };
    }
}
