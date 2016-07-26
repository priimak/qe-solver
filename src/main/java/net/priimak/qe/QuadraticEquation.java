package net.priimak.qe;

/**
 * Immutable class that represent quadratic equation ax^2 + bx + c = 0
 */
public final class QuadraticEquation {
    private static long SIGN_MASK = 0x8000000000000000L;
    private static long EXPONENT_MASK = 0x7ff0000000000000L;
    private static long MANTISSA_MASK = 0x000fffffffffffffL;

    private final double b;
    private final double a;
    private final double c;

    /**
     * Create instance of {@code QuadraticEquation} where parameters a, b and c represent parameters
     * in the equation ax^2 + bx + c = 0
     */
    public QuadraticEquation(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
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
}
