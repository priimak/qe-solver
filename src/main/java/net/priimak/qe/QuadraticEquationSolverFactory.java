package net.priimak.qe;

/**
 *
 */
public class QuadraticEquationSolverFactory {
    static final double[] NO_SOLUTIONS = new double[0];

    private QuadraticEquationSolverFactory() {
        throw new AssertionError();
    }

    public static QuadraticEquationSolver getSolver(QuadraticEquationSolver.Type type) {
        switch (type) {
            case SIMPLE:
                return SimpleQuadraticEquationSolver.INSTANCE;
            case CITARDAUQ:
                return CitardauqQuadraticEquationSolver.INSTANCE;
            case SIMPLE_AP:
                return SimpleApQuadraticEquationSolver.INSTANCE;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Check that number provided as a parameter is finite, i.e. that neither overflow not division by zero took place.
     *
     * @param x number to check for been finite
     * @return that very same number that was passed as an argument
     * @throws OutOfNumericRange if number is not finite.
     */
    static double ensureFiniteNumber(double x) throws OutOfNumericRange {
        if (!Double.isFinite(x)) {
            throw new OutOfNumericRange(
                "Parameters a, b and c are out of range where solution with this precession is possible."
            );
        } else {
            return x;
        }
    }

    /**
     * Handler corner cases in the ax^2 + bx + c = 0 equation where any of the a, b or c parameters are 0.
     *
     * @return null if all three of the a, b and c parameters are not 0 otherwise return array of size 0, 1 or 2
     *         containing real roots of quadratic equation.
     * @throws OutOfNumericRange if numeric overflow occurred.
     */
    static double[] handleCornerCases(double a, double b, double c) throws OutOfNumericRange {
        if (a == 0) {
            if (b == 0) {
                // c = 0
                return NO_SOLUTIONS;
            } else if (c == 0) {
                // bx = 0
                return new double[] {0};
            } else {
                // bx + c = 0
                return new double[] {ensureFiniteNumber(- c / b)};
            }
        } else if (b == 0) {
            if (c == 0) {
                // ax^2 = 0
                return new double[] {0};
            } else if ((c > 0 && a > 0) || (c < 0 && a < 0)){
                return NO_SOLUTIONS;
            } else {
                // ax^2 + c = 0, -c/a > 0
                return new double[] {ensureFiniteNumber(- c / a)};
            }
        } else if (c == 0) {
            // ax^2 + bx = 0
            return new double[] {0, ensureFiniteNumber( - b / a)};
        } else {
            return null;
        }
    }
}
