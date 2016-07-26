package net.priimak.qe;

/**
 * Represents function/solver that takes {@link QuadraticEquation} and computes its roots. All solvers implementing
 * this interface must be immutable and idempotent.
 */
@FunctionalInterface
public interface QuadraticEquationSolver {
    /**
     * Types of quadratic equation solvers that can be produced by the {@link QuadraticEquationSolverFactory}.
     */
    enum Type {
        /**
         * Type of solution that relies on the fact that x_1 * x_2 = c / a, which it uses to compute of root of
         * quadratic equation avoiding catastrophic cancelation.
         */
        CITARDAUQ,

        /**
         * Simple method relying on the well known formulae x_{1,2} = \frac{-b \pm \sqrt{b^2-4ac}}{2a}
         */
        SIMPLE,

        /**
         * Same method is as {@link #SIMPLE} but uses Apfloat library.
         */
        SIMPLE_AP
    };

    /**
     * Solves quadratic equation ax^2 + bx + c = 0.
     *
     * @param equation quadratic equation to solve.
     * @return arrays of doubles of size 0 if no real solutions exists and size 1 or 2 containing roots of the equation.
     * @throws OutOfNumericRange if computation of the roots resulted in the numeric overflow.
     */
    double[] solve(QuadraticEquation equation) throws OutOfNumericRange;
}
