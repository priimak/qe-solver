package net.priimak.qe;

/**
 * Immutable class that represent quadratic equation ax^2 + bx + c = 0
 */
public final class QuadraticEquation {
    private final double a;
    private final double b;
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
}
