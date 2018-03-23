package ch.sharpsoft.hexapod;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer;

/**
 * <code>
 *    S 1.5 / alpha - yaw
 *  --O beta pitch
 *     \
 *      \ 10.5
 *       \
 *        \
 *         O gamma pitch
 *         |
 *         |
 *         | 9.5
 *         |
 *         O epsilon pitch
 *         |
 *         |
 *         | 17
 *         |
 *         |
 *         E       
 * </code>
 * 
 * breite = 5.5cm höhe = 4cm
 */
public class RoboterArm {
    public final static double WIDTH = 5.5;
    public final static double HEIGHT = 4;
    private final static SecureRandom sr = new SecureRandom();
    private final int id;
    private final Vector3 startPoint;
    private final Quaternion startOrientation;
    private final LegSegment startSegment;
    private final LegSegment segment1;
    private final LegSegment segment2;
    private final LegSegment segment3;
    private final LegSegment endSegment;
    private final Knee k1;
    private final Knee k2;
    private final Knee k3;
    private final Knee k4;
    private final double maxLength;

    private final static List<Vector3> base = new ArrayList<>();
    static {
        base.add(new Vector3(0, -WIDTH / 2, HEIGHT / 2));
        base.add(new Vector3(0, WIDTH / 2, HEIGHT / 2));
        base.add(new Vector3(0, -WIDTH / 2, -HEIGHT / 2));
        base.add(new Vector3(0, WIDTH / 2, -HEIGHT / 2));
    }

    public RoboterArm(final int id, final Vector3 startPoint) {
        this.id = id;
        this.startOrientation = Quaternion.fromEuler(-Math.PI / 2, -Math.PI / 2, 0);
        this.startPoint = startPoint;
        startSegment = new LegSegment(startPoint, 0.0, startOrientation);
        segment1 = new LegSegment(startPoint.add(startSegment.getVector()), 1.5, startOrientation);
        segment2 = new LegSegment(segment1.getStartPoint().add(segment1.getVector()), 10.5, startOrientation);
        segment3 = new LegSegment(segment2.getStartPoint().add(segment2.getVector()), 9.5, startOrientation);
        endSegment = new LegSegment(segment3.getStartPoint().add(segment3.getVector()), 17, startOrientation);
        k1 = new Knee(startSegment, segment1, true);
        k2 = new Knee(segment1, segment2, false);
        k3 = new Knee(segment2, segment3, false);
        k4 = new Knee(segment3, endSegment, false);
        maxLength = segment1.getLength() + segment2.getLength() + segment3.getLength() + endSegment.getLength();
    }

    public void setAngles(final double alpha, final double beta, final double gamma, final double epsilon) {
        this.k1.setAngle(alpha);
        this.k2.setAngle(beta);
        this.k3.setAngle(gamma);
        this.k4.setAngle(epsilon);
        final Quaternion o1 = Quaternion.fromEuler(0.0, 0.0, alpha).multiply(startOrientation);
        final Quaternion o2 = o1.multiply(Quaternion.fromEuler(0.0, beta, 0.0));
        final Quaternion o3 = o2.multiply(Quaternion.fromEuler(0.0, gamma, 0.0));
        final Quaternion o4 = o3.multiply(Quaternion.fromEuler(0.0, epsilon, 0.0));
        segment1.setOrientation(o1);
        segment2.setOrientation(o2);
        segment3.setOrientation(o3);
        endSegment.setOrientation(o4);
        segment2.setStartPoint(segment1.getEndPoint());
        segment3.setStartPoint(segment2.getEndPoint());
        endSegment.setStartPoint(segment3.getEndPoint());
    }

    public void setEndpoint(final Vector3 vector3) {
        double bond = Math.PI;
        final double[] initialAngles = getAngles();
        MultivariateFunction f = new MultivariateFunction() {

            @Override
            public double value(double[] point) {
                try {
                    setAngles(point[0], point[1], point[2], point[3]);
                } catch (Exception e) {
                    return 10000000;
                }
                if (Math.abs(point[0]) > bond - 0.1
                        || Math.abs(point[1]) > bond - 0.1
                        || Math.abs(point[2]) > bond - 0.1
                        || Math.abs(point[3]) > bond - 0.1) {
                    return 1000000;
                }

                double normSquared = getEndpoint().substract(vector3).normSquared();
                return normSquared
                        + (Math.abs(point[0])
                                + Math.abs(point[1])
                                + Math.abs(point[2])
                                + Math.abs(point[3])) * 0.01;// +
                                                             // getEndpoint().normSquared();
            }

        };

        double initialTrustRegionRadius = 0.05;
        int retry = 10;
        while (retry-- > -10) {
            try {
                PointValuePair result = solve(bond, initialAngles, f, initialTrustRegionRadius);
                double[] resultat = result.getPoint();
                setAngles(resultat[0], resultat[1], resultat[2], resultat[3]);
                if (getEndpoint().substract(vector3).normSquared() < 0.01) {
                    return;
                }
            } catch (Exception e) {
                // System.out.println(e.getMessage());
                initialTrustRegionRadius *= 2;
            }
            if (retry > 5) {
                setAngles(0.0, 0.0, 0.0, 0.0);
                initialAngles[0] = 0;
                initialAngles[1] = 0;
                initialAngles[2] = 0;
                initialAngles[3] = 0;
            } else {
                double alpha = getNextAngle();
                double beta = getNextAngle();
                double gamma = getNextAngle();
                double epsilon = getNextAngle();

                setAngles(alpha, beta, gamma, epsilon);
                initialAngles[0] = alpha;
                initialAngles[1] = beta;
                initialAngles[2] = gamma;
                initialAngles[3] = epsilon;
            }
        }
        throw new IllegalArgumentException("Solver could not find solution " + vector3);
    }

    private static double getNextAngle() {
        return (sr.nextDouble() - 0.5) * Math.PI;
    }

    private PointValuePair solve(double bond, final double[] initialAngles, MultivariateFunction f, double initialTrustRegionRadius) {
        MultivariateOptimizer optimize = new BOBYQAOptimizer((5 * 6) / 2, initialTrustRegionRadius, 0.0000000000001);
        PointValuePair result = optimize.optimize(
                new MaxEval(200000),
                GoalType.MINIMIZE,
                new InitialGuess(initialAngles),
                new ObjectiveFunction(f),
                new SimpleBounds(new double[] { -bond, -bond, -bond, -bond }, new double[] { bond, bond, bond, bond }));
        return result;
    }

    public double[] getAngles() {
        return new double[] { k1.getAngle(), k2.getAngle(), k3.getAngle(), k4.getAngle() };
    }

    /**
     * Each box has 8 Points around it's vector. <code>
     *        (+z)
     *         . b1(+y)    . b5
     * (-y)b0 '        b4 ' 
     *        |           |
     *        s - - - - ->e
     *        |           |
     *     b2 .        b6 .
     *         ' b3        ' b7
     *       (-z)
     * </code>
     */
    public List<List<Vector3>> getBoundingBoxes() {
        List<List<Vector3>> result = new ArrayList<>();
        result.add(getBoundingBoxOfSegment(base, segment1));
        result.add(getBoundingBoxOfSegment(base, segment2));
        result.add(getBoundingBoxOfSegment(base, segment3));
        result.add(getBoundingBoxOfSegment(base, endSegment));
        return result;
    }

    private List<Vector3> getBoundingBoxOfSegment(List<Vector3> base, LegSegment s) {
        List<Vector3> s0 = new ArrayList<>();
        base.forEach(b -> s0.add(s.getOrientation().multiply(b).add(s.getStartPoint())));
        base.forEach(b -> s0.add(s.getOrientation().multiply(b).add(s.getEndPoint())));
        return s0;
    }

    public Vector3 getEndpoint() {
        return getEndSegment().getEndPoint();
    }

    public int getId() {
        return id;
    }

    public Vector3 getStartPoint() {
        return startPoint;
    }

    public Quaternion getStartOrientation() {
        return startOrientation;
    }

    public LegSegment getStartSegment() {
        return startSegment;
    }

    public LegSegment getSegment1() {
        return segment1;
    }

    public LegSegment getSegment2() {
        return segment2;
    }

    public LegSegment getSegment3() {
        return segment3;
    }

    public LegSegment getEndSegment() {
        return endSegment;
    }

    public Knee getK1() {
        return k1;
    }

    public Knee getK2() {
        return k2;
    }

    public Knee getK3() {
        return k3;
    }

    public Knee getK4() {
        return k4;
    }

    public double getMaxLength() {
        return maxLength;
    }

    @Override
    public String toString() {
        double[] angles = getAngles();
        return "RA[" + getEndpoint() + " angles[" + angles[0] + "," + angles[1] + "," + angles[2] + "," + angles[3] + "]]";
    }
}