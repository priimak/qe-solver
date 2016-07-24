package net.priimak.qe;

enum CitardauqQuadraticEquationSolver implements QuadraticEquationSolver {
    /**
     * Instance of {@link CitardauqQuadraticEquationSolver} that solves quadratic equation using Citardauq formulae.
     * See {@link QuadraticEquationSolver.Type#CITARDAUQ} for more details.
     */
    INSTANCE;

    @Override
    public double[] solve(QuadraticEquation equation) throws OutOfNumericRange {
        double a = equation.getA();
        double b = equation.getB();
        double c = equation.getC();
        double[] result = QuadraticEquationSolverFactory.handleCornerCases(a, b, c);
        if (result != null) {
            return result;
        } else {
            // ax^2 + bx + c = 0
            double discriminant = QuadraticEquationSolverFactory.ensureFiniteNumber(b * b - 4 * a * c);
            if (discriminant < 0) {
                return QuadraticEquationSolverFactory.NO_SOLUTIONS;
            } else if (discriminant == 0) {
                return new double[] {QuadraticEquationSolverFactory.ensureFiniteNumber(- (b / a) / 2)};
            }
            if (b > 0) {
                double discriminantPart = -b - Math.sqrt(discriminant);
                return new double[] {
                    QuadraticEquationSolverFactory.ensureFiniteNumber(2 * c / discriminantPart),
                    QuadraticEquationSolverFactory.ensureFiniteNumber(discriminantPart / a / 2)
                };
            } else { // b < 0
                double discriminantPart = -b + Math.sqrt(discriminant);
                return new double[] {
                    QuadraticEquationSolverFactory.ensureFiniteNumber(discriminantPart / a / 2),
                    QuadraticEquationSolverFactory.ensureFiniteNumber(2 * c / discriminantPart)
                };
            }
        }
    }
}
