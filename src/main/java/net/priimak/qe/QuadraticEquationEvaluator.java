package net.priimak.qe;

/**
 * Utility class that contains evaluation functions that operate on the {@link QuadraticEquation}.
 */
public final class QuadraticEquationEvaluator {
    private static final long APFLOAT_PRECISION = 100;

    private QuadraticEquationEvaluator() {
        throw new AssertionError();
    }

    /**
     * Compute value of given quadratic formulae y = ax^2 + bx + c for a given value of x.
     *
     * @param equation equation/formulae to compute value of.
     * @param x x value for which evaluation quadratic formulae.
     * @return value of the quadratic formulae computed at point {@code x}.
     */
    public static double compute(QuadraticEquation equation, double x) {
        return (equation.getA() * x + equation.getB()) * x + equation.getC();
    }
}
