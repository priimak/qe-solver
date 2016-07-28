package net.priimak.qe;

import org.apfloat.Apfloat;

/**
 * Utility class that contains evaluation functions that operate on the {@link QuadraticEquation}.
 */
public final class QuadraticEquationEvaluator {
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
        return compute(equation, new Apfloat(x)).doubleValue();
    }

    public static Apfloat compute(QuadraticEquation equation, Apfloat x) {
        return new Apfloat(String.format("%s", equation.getA()), NewQadraticEquationSolver.APFLOAT_PRECESSION).multiply(x).multiply(x)
            .add(new Apfloat(String.format("%s", equation.getB()), NewQadraticEquationSolver.APFLOAT_PRECESSION).multiply(x))
            .add(new Apfloat(String.format("%s", equation.getC()), NewQadraticEquationSolver.APFLOAT_PRECESSION));
    }
}
