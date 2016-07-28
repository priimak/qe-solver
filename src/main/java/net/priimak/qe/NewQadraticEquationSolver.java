package net.priimak.qe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.priimak.numeric.Value;
import org.apfloat.Apcomplex;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public enum NewQadraticEquationSolver {
    INSTANCE;

    public static final int APFLOAT_PRECESSION = 1000;
    private static final Apfloat TWO = new Apfloat("2", APFLOAT_PRECESSION);

    List<Value<Double>> solve(QuadraticEquation equation, double maxRelativeError) throws OutOfNumericRange {
        QuadraticEquation simplifedEquation = QuadraticEquation.simplify(equation);
        double[] roots = CitardauqQuadraticApEquationSolver.INSTANCE.solve(simplifedEquation);
        if (roots.length == 0) {
            return Collections.emptyList();
        }

        ArrayList<Apfloat> rootz = new ArrayList<>(roots.length);
        for (double root : roots) {
            rootz.add(new Apfloat(String.format("%s", root), Apcomplex.INFINITE));
        }

        final Apfloat relativeErrorTarget = new Apfloat(maxRelativeError, APFLOAT_PRECESSION);
        return rootz.stream().map(root -> {
            Apfloat value = QuadraticEquationEvaluator.compute(equation, root);
            if (value.compareTo(Apcomplex.ZERO) == 0) {
                double theRoot = root.doubleValue();
                Apfloat valueInDouble = QuadraticEquationEvaluator.compute(equation, new Apfloat(theRoot));
                return new Value<Double>(
                    theRoot,
                    valueInDouble.compareTo(Apcomplex.ZERO) == 0 ?
                        0 :
                        Math.max(
                            Math.abs(new Apfloat(String.format("%s", theRoot), Apcomplex.INFINITE)
                                .subtract(root).doubleValue()),
                            QuadraticEquation.getDelta(theRoot)
                        )
                );
            } else {
                Apfloat x = root;
                for (;;) {
                    Apfloat derivative = new Apfloat(String.format("%s", equation.getA()), Apcomplex.INFINITE)
                        .multiply(TWO).multiply(new Apfloat(x.toString(), Apcomplex.INFINITE))
                        .add(new Apfloat(String.format("%s", equation.getB()), Apcomplex.INFINITE));
                    if (derivative.compareTo(Apcomplex.ZERO) == 0) {
                        Apfloat otheRoot = theOtherRoot(rootz, root);
                        if (otheRoot.equals(root) || otheRoot.compareTo(root) > 0) {
                            x = otheRoot.subtract(new Apfloat("0.1", Apcomplex.INFINITE));
                        } else {
                            x = otheRoot.add(new Apfloat("0.1", Apcomplex.INFINITE));
                        }
                        continue;
                    }
                    Apfloat xNext = x.subtract(value.divide(derivative));
                    Apfloat delta = xNext.subtract(x);
                    if (ApfloatMath.abs(delta.divide(x)).compareTo(relativeErrorTarget) < 0) {
                        double theRoot = xNext.doubleValue();
                        Apfloat valueInDouble = QuadraticEquationEvaluator.compute(equation, new Apfloat(theRoot));
                        return new Value<Double>(
                            theRoot,
                            valueInDouble.compareTo(Apcomplex.ZERO) == 0 ?
                                0 :
                                Math.max(
                                    Math.abs(new Apfloat(String.format("%s", theRoot), Apcomplex.INFINITE)
                                        .subtract(x).doubleValue()),
                                    QuadraticEquation.getDelta(theRoot)
                                )
                        );
                    }
                    value = QuadraticEquationEvaluator.compute(equation, xNext);
                    x = xNext;
                }
            }
        }).collect(Collectors.toList());
    }

    private static Apfloat theOtherRoot(List<Apfloat> roots, Apfloat root) {
        for (Apfloat theRoot : roots) {
            if (!theRoot.equals(root)) {
                return theRoot;
            }
        }
        return root;
    }
}
