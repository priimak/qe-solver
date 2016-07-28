package net.priimak.qe;

enum SimpleQuadraticEquationSolver implements QuadraticEquationSolver {
    /**
     * Instance of {@link SimpleQuadraticEquationSolver} that solves quadratic equation using simple formulae.
     * Before computing root, equation is simplified by passing it through
     * {@link QuadraticEquation#simplify(QuadraticEquation)}.
     * See {@link QuadraticEquationSolver.Type#SIMPLE} for more details.
     */
    INSTANCE;

    @Override
    public double[] solve(QuadraticEquation equation) throws OutOfNumericRange {
        double[] result = QuadraticEquationSolverFactory.handleCornerCases(equation);
        if (result != null) {
            return result;
        } else {
            QuadraticEquation simplifiedEquation = QuadraticEquation.simplify(equation);
            double a = simplifiedEquation.getA();
            double b = simplifiedEquation.getB();
            double c = simplifiedEquation.getC();

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
