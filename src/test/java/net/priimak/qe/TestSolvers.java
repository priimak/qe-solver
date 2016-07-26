package net.priimak.qe;

import java.util.Arrays;
import java.util.List;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
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

        new QuadraticEquation(-1e-200, 3e-200, 2e-199)
    );

    /**
     * If root != 0 then we relative error, i.e. abs(y(root)/root) must less than this this value.
     */
    private static Apfloat MAX_RELATIVE_ERROR = new Apfloat(0.1, 100);

    /**
     * If root = 0 then numerical deviation of quadratic equation at zero must not be greater than this value.
     */
    private static Apfloat MAX_ZERO_ERROR = new Apfloat(1.0E-11, 100);

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
                QuadraticEquation eq = QuadraticEquation.simplify(equation);
                double[] roots = solver.solve(eq);
                for (double root : roots) {
                    double value = QuadraticEquationEvaluator.compute(eq, root);
                    System.out.println(equation + " : root = " + root + " -> " + value);
                    Apfloat y = new Apfloat(value, 100);
                    if (root == 0) {
                        Assert.assertEquals(ApfloatMath.abs(y).compareTo(MAX_ZERO_ERROR) < 0, true,
                            equation + " @ x = " + root + " -> " + y);
                    } else {
                        Assert.assertEquals(
                            ApfloatMath.abs(y.divide(new Apfloat(root, 100))).compareTo(MAX_RELATIVE_ERROR) < 0,
                            true,
                            equation + " : x = " + root + " -> " + y
                        );
                    }
                }
            } catch (OutOfNumericRange outOfNumericRange) {
                Assert.fail(outOfNumericRange.getMessage());
            }
        }
    }
}
