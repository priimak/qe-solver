package net.priimak.qe;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

enum SimpleApQuadraticEquationSolver implements QuadraticEquationSolver {
    INSTANCE;

    private static final Apfloat TWO = new Apfloat(2, 100);
    private static final Apfloat MINUTS_ONE = new Apfloat(-1, 100);

    @Override
    public double[] solve(QuadraticEquation equation) throws OutOfNumericRange {
        Apfloat a = new Apfloat(equation.getA(), 100);
        Apfloat b = new Apfloat(equation.getB(), 100);
        Apfloat c = new Apfloat(equation.getC(), 100);
        double[] result = QuadraticEquationSolverFactory.handleCornerCases(equation.getA(), equation.getB(), equation.getC());
        if (result != null) {
            return result;
        } else {
            // ax^2 + bx + c = 0
            Apfloat discriminant = b.multiply(b).subtract(new Apfloat(4, 100).multiply(a).multiply(c));
            int cmp = discriminant.compareTo(new Apfloat(0, 100));
            if (cmp == 0) {
                // only one solution
                Apfloat root = b.divide(a).divide(new Apfloat(-2, 100));
                return new double[] {root.doubleValue()};
            } else if (cmp < 0){
                return QuadraticEquationSolverFactory.NO_SOLUTIONS;
            } else {
                // two solutions
                return new double[] {
                    MINUTS_ONE.multiply(b).add(ApfloatMath.root(discriminant, 2))
                        .divide(a).divide(TWO).doubleValue(),
                    MINUTS_ONE.multiply(b).subtract(ApfloatMath.root(discriminant, 2))
                        .divide(a).divide(TWO).doubleValue()
                };
            }
        }
    }
}
