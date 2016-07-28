package net.priimak.qe;

import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

enum CitardauqQuadraticApEquationSolver implements QuadraticEquationSolver {
    INSTANCE;

    @Override
    public double[] solve(QuadraticEquation equation) throws OutOfNumericRange {
        double[] result = QuadraticEquationSolverFactory.handleCornerCases(equation);
        if (result != null) {
            return result;
        } else {
            QuadraticEquation simplifiedEquation = QuadraticEquation.simplify(equation);
            Apfloat a = simplifiedEquation.getaAp();
            Apfloat b = simplifiedEquation.getbAp();
            Apfloat c = simplifiedEquation.getcAp();
            // ax^2 + bx + c = 0
            Apfloat discriminant = b.multiply(b).subtract(new Apfloat("4", Apcomplex.INFINITE)).multiply(a).multiply(c);
            if (discriminant.compareTo(Apcomplex.ZERO) < 0) {
                return QuadraticEquationSolverFactory.NO_SOLUTIONS;
            } else if (discriminant.compareTo(Apcomplex.ZERO) == 0) {
                return new double[] {b.divide(a).divide(new Apfloat("-2", Apcomplex.INFINITE)).doubleValue()};
            }
            if (b.compareTo(Apcomplex.ZERO) > 0) {
                Apfloat discriminantPart = new Apfloat("0", Apcomplex.INFINITE).subtract(b.add(ApfloatMath.sqrt(discriminant)));
                return new double[] {
                    new Apfloat("2", Apcomplex.INFINITE).multiply(c).divide(discriminantPart).doubleValue(),
                    discriminantPart.divide(a).divide(new Apfloat("2", Apcomplex.INFINITE)).doubleValue()
                };
            } else { // b < 0
                Apfloat discriminantPart = new Apfloat("0", Apcomplex.INFINITE).subtract(b.subtract(ApfloatMath.sqrt(discriminant)));
                return new double[] {
                    discriminantPart.divide(a).divide(new Apfloat("2", Apcomplex.INFINITE)).doubleValue(),
                    new Apfloat("2", Apcomplex.INFINITE).multiply(c).divide(discriminantPart).doubleValue()
                };
            }
        }
    }
}
