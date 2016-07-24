This is a library containing classes and method for solving quadratic equations of the form

    ax^2 + bx + c = 0

Library uses java primitive datatype of double to define and solve these equations and can be used like so

    QuadraticEquation equation = new QuadraticEquation(-1.0E-30, 1.1E122D, 8.0D);

    QuadraticEquationSolver.Type solverType = QuadraticEquationSolver.Type.CITARDAUQ;
    QuadraticEquationSolver solver = QuadraticEquationSolverFactory.getSolver(solverType);

    double[] roots = solver.solve(equation);

Two type of solver are provided CITARDAUQ and SIMPLE, the later one relies on the common high school equation to compute
roots of quadratic equations and former one relies on Citardauq formulas to advoid catastrophic cancelation due to the
limited precision of double datatype.

For testing this library relies on the arbitrary precision library Apfloat at the precision of 100.

Please see class TestSolvers where you can add other other quadratic equations for testing.


