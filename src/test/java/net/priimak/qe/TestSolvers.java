package net.priimak.qe;

import java.util.Arrays;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public final class TestSolvers {
    /**
     * We will be testing different solvers against these equations. Please add more here if so desired.
     */
    private static final List<QuadraticEquation> EQUATIONS = Arrays.asList(
        new QuadraticEquation(-1.0E-30, 1.1E122D, 8.0D),
        new QuadraticEquation(Math.PI, 1.12312412414E122D, 1.298791481E-100D),
        new QuadraticEquation(Math.PI * 1.0E-100, 1.12312412414E122D, 1.298791481E-100D),
        new QuadraticEquation(Math.PI * 1.0E-100, 1.12312412414E-22D, 1.298791481E-100D),
        new QuadraticEquation(-1, 1, 1),
        new QuadraticEquation(0, 0, 0),
        new QuadraticEquation(1, 1, 1),
        new QuadraticEquation(1, 1, 0),
        new QuadraticEquation(1, 0, 1),
        new QuadraticEquation(0, 1, 1),
        new QuadraticEquation(0, 0, 1),
        new QuadraticEquation(0, 1, 0),
        new QuadraticEquation(1, 0, 0),

        new QuadraticEquation(1, -3, 2),
        new QuadraticEquation(1e200, -3e200, 2e200),
        new QuadraticEquation(1e-200, -3e-200, 2e-200),
        new QuadraticEquation(-1e200, 3e200, -2e200),
        new QuadraticEquation(-1e-200, 3e-200, -2e-200),

        new QuadraticEquation(-1e-200, 3e-200, 2e-199),
        new QuadraticEquation(-1e-200, 3e-201, 2e97)
    );

    /**
     * Maximum relative error of the computed root computed by the dividing error by the value of the root or the
     * smallest possible value for double.
     */
    private static final double MAXIMUM_RELATIVE_ERROR = 0.01;

    /**
     * This test will fail due to significant numerical
     * error in {@link net.priimak.qe.SimpleQuadraticEquationSolver#INSTANCE}. Enable it to see just that.
     */
    @Test(enabled = false)
    public void testSimpleSolver() {
        testSolver(QuadraticEquationSolverFactory.getSolver(QuadraticEquationSolver.Type.SIMPLE_AP));
    }

    @Test
    public void testCitardauqSolver() {
        testSolver(QuadraticEquationSolverFactory.getSolver(QuadraticEquationSolver.Type.CITARDAUQ));
    }

    private static void testSolver(QuadraticEquationSolver solver) {
        System.out.println(String.format("\nTesting %s", solver.getClass().getSimpleName()));
        for (QuadraticEquation equation : EQUATIONS) {
            try {
                double[] roots = QuadraticEquation.deduceError(equation, solver.solve(equation));
                for (int i = 0; i < roots.length / 2; i++) {
                    double root = roots[i];
                    double value = QuadraticEquationEvaluator.compute(equation, root);
                    double error = roots[roots.length / 2 + i];
                    System.out.println(equation + " : root = [" + root + " ± " + error + "] -> " + value);
                    double relativeError = Math.abs(error / (root == 0 ? Double.MIN_VALUE : root));
                    Assert.assertEquals(relativeError < MAXIMUM_RELATIVE_ERROR, true,
                        String.format("Relative error %s is greater than %s", relativeError, MAXIMUM_RELATIVE_ERROR));
                }
            } catch (OutOfNumericRange outOfNumericRange) {
                Assert.fail(outOfNumericRange.getMessage());
            }
        }
    }
}
