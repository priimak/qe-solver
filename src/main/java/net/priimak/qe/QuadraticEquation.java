package net.priimak.qe;

import org.apfloat.Apfloat;

/**
 * Immutable class that represent quadratic equation ax^2 + bx + c = 0
 */
public final class QuadraticEquation {
    public static final int MAX_SEEK_ERROR_ITERATIONS = 10000;
    private static long SIGN_MASK = 0x8000000000000000L;
    private static long EXPONENT_MASK = 0x7ff0000000000000L;
    private static long MANTISSA_MASK = 0x000fffffffffffffL;

    private final double a;
    private final double b;
    private final double c;

    private final Apfloat aAp;
    private final Apfloat bAp;
    private final Apfloat cAp;

    /**
     * Create instance of {@code QuadraticEquation} where parameters a, b and c represent parameters
     * in the equation ax^2 + bx + c = 0
     */
    public QuadraticEquation(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;

        aAp = new Apfloat(a);
        bAp = new Apfloat(b);
        cAp = new Apfloat(c);
    }

    @Override
    public String toString() {
        return String.format("%s * x^2 + %s * x + %s", a, b, c);
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public Apfloat getaAp() {
        return aAp;
    }

    public Apfloat getbAp() {
        return bAp;
    }

    public Apfloat getcAp() {
        return cAp;
    }

    public static long exponent(double number) {
        return ((Double.doubleToLongBits(number) & EXPONENT_MASK) >> 52);
    }

    /**
     * Simplify quadratic equation removing common exponents where possible.
     *
     * @param equation equation to simplify
     * @return simplified equation.
     */
    public static QuadraticEquation simplify(QuadraticEquation equation) {
        long aExp = exponent(equation.getA());
        long bExp = exponent(equation.getB());
        long cExp = exponent(equation.getC());

        long minExp = Math.min(Math.min(aExp, bExp), cExp) - 1023;
        double div = Math.pow(2.0D, minExp);
        QuadraticEquation simplified = new QuadraticEquation(equation.getA() / div, equation.getB() / div, equation.getC() / div);
        if (minExp < 0
            && (exponent(simplified.getA()) > 1044
                || exponent(simplified.getB()) > 1044
                || exponent(simplified.getC()) > 1044)) {
            // return original as we might reduce precision of the solution by such "simplification"
            return equation;
        } else {
            return simplified;
        }
    }

    /**
     * Very slow method of deducing error of the solution of the quadratic equation. Might fail producing error value
     * of -1 if number of iterations needed to compute error is greater than 10000.
     */
    public static double[] deduceError(QuadraticEquation equation, double[] roots) {
        if (roots.length == 0) {
            return roots;
        } else {
            double[] result = new double[2 * roots.length];
            for(int i = 0; i < roots.length; i++) {
                result[i] = roots[i];
            }
            int index = roots.length;
            for(double root : roots) {
                double value = QuadraticEquationEvaluator.compute(equation, root);
                if (value == 0) {
                    result[index++] = 0;
                } else {
                    result[index++] = findErrorForRoot(equation, root);
                }
            }
            return result;
        }
    }

    private static double findErrorForRoot(QuadraticEquation equation, double root) {
        double value = QuadraticEquationEvaluator.compute(equation, root);
        double signAtRoot = Math.signum(value);

        double leftProbe = root;
        double rightProbe = root;
        for(int i = 1; i < MAX_SEEK_ERROR_ITERATIONS; i++) {
            if (value == 0) {
                return 0;
            } else {
                leftProbe -= getDelta(leftProbe);
                rightProbe += getDelta(rightProbe);
                if (Math.signum(QuadraticEquationEvaluator.compute(equation, leftProbe)) != signAtRoot) {
                    return root - leftProbe;
                } else if (Math.signum(QuadraticEquationEvaluator.compute(equation, rightProbe)) != signAtRoot) {
                    return rightProbe - root;
                }
            }
        }
        return -1;
    }

    public static double getDelta(double number) {
        long exp = Double.doubleToLongBits(number) & EXPONENT_MASK;
        double nextNearest = Double.longBitsToDouble(exp | 0x0000000000000001L);
        return nextNearest - Double.longBitsToDouble(exp);
    }
}
