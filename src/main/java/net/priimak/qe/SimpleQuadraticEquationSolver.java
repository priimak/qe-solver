package net.priimak.qe;

enum SimpleQuadraticEquationSolver implements QuadraticEquationSolver {
    /**
     * Instance of {@link SimpleQuadraticEquationSolver} that solves quadratic equation using simple formulae.
     * See {@link QuadraticEquationSolver.Type#SIMPLE} for more details.
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
            if (discriminant == 0) {
                // only one solution
                return new double[] {QuadraticEquationSolverFactory.ensureFiniteNumber( - ( b / a )/ 2)};
            } else if (discriminant < 0){
                return QuadraticEquationSolverFactory.NO_SOLUTIONS;
            } else {
                // two solutions
                return new double[] {
                    QuadraticEquationSolverFactory.ensureFiniteNumber((-b + Math.sqrt(discriminant))/a)/2,
                    QuadraticEquationSolverFactory.ensureFiniteNumber((-b - Math.sqrt(discriminant))/a)/2
                };
            }
        }
    }
}
