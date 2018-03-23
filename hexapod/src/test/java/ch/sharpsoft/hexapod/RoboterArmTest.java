package ch.sharpsoft.hexapod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Collections;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class RoboterArmTest {

    private final static SecureRandom sr = new SecureRandom();

    @Test
    public void testYawPlain() {
        RoboterArm ra = new RoboterArm(1, new Vector3(0, 0, 0));
        for (double d = -4 * Math.PI; d < 4 * Math.PI; d += 0.01) {
            ra.setAngles(d, 0, 0, 0);
            assertEquals(0.0, ra.getEndpoint().getX(), 0.001);
            assertEquals(0.0, ra.getEndpoint().getY(), 0.001);
            assertEquals(38.5, ra.getEndpoint().getZ(), 0.001);
        }
    }

    @Ignore
    @Test
    public void testRandom() throws IOException {
        RoboterArm ra = new RoboterArm(1, new Vector3(0, 0, 0));
        ra.setAngles(0.0, Math.PI / 2, 0, 0);
        ra.setAngles(0.0, 0.0, Math.PI / 2, 0);
        ra.setAngles(0.0, 0.0, 0.0, Math.PI / 2);
        ra.setAngles(0.3, 0.0, 0.0, Math.PI / 2);
        int i = 1000000;
        while (i-- > 0) {
            double alpha = getNextAngle();
            double beta = getNextAngle();
            double gamma = getNextAngle();
            double epsilon = getNextAngle();
            doAngles(ra, alpha, beta, gamma, epsilon);
            doAngles(ra, -alpha, beta, gamma, epsilon);
            doAngles(ra, -alpha, -beta, -gamma, -epsilon);
            doAngles(ra, alpha, -beta, -gamma, -epsilon);
        }
    }

    @Test
    public void testIncremental() throws IOException {
        RoboterArm ra1 = new RoboterArm(1, new Vector3(0, 0, 0));
        RoboterArm ra2 = new RoboterArm(1, new Vector3(0, 0, 0));
        double alpha = 0.0;
        double beta = 0.0;
        double gamma = 0.0;
        double epsilon = 0.0;
        int i = 1000;
        while (i-- > 0) {
            ra2.setAngles(alpha, beta, gamma, epsilon);
            alpha = (alpha + ((sr.nextDouble() - 0.5) * 0.3)) % (Math.PI - 0.5);
            beta = (beta + ((sr.nextDouble() - 0.5) * 0.3)) % (Math.PI - 0.5);
            gamma = (gamma + ((sr.nextDouble() - 0.5) * 0.3)) % (Math.PI - 0.5);
            epsilon = (epsilon + ((sr.nextDouble() - 0.5) * 0.3)) % (Math.PI - 0.5);
            ra1.setAngles(alpha, beta, gamma, epsilon);
            Vector3 endpoint = ra1.getEndpoint();
            // long start = System.nanoTime();
            ra2.setEndpoint(endpoint);
            // long end = System.nanoTime();
            // System.out.println((end - start) / (double)
            // TimeUnit.MILLISECONDS.toNanos(1));
            // System.out.println();
            // System.out.println(ra1);
            // System.out.println(ra2);
            // System.out.println(ra1.getEndpoint().substract(ra2.getEndpoint()));
            assertEquals(0.0, ra1.getEndpoint().substract(ra2.getEndpoint()).normSquared(), 0.01);
        }
    }

    @Test
    public void testRandomOnlyEndpoints() throws IOException {
        RoboterArm ra1 = new RoboterArm(1, new Vector3(0, 0, 0));
        RoboterArm ra2 = new RoboterArm(1, new Vector3(0, 0, 0));
        int i = 1000;
        while (i-- > 0) {
            double alpha = getNextAngle();
            double beta = getNextAngle();
            double gamma = getNextAngle();
            double epsilon = getNextAngle();
            ra1.setAngles(alpha, beta, gamma, epsilon);
            Vector3 endpoint = ra1.getEndpoint();
            try {
                ra2.setEndpoint(endpoint);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
                System.out.println(ra1);
                System.out.println(ra2);
                System.out.println(ra1.getEndpoint().substract(ra2.getEndpoint()));
            }
            assertEquals(0.0, ra1.getEndpoint().substract(ra2.getEndpoint()).normSquared(), 0.01);
        }
    }

    private static void doAngles(RoboterArm ra, double alpha, double beta, double gamma, double epsilon) throws IOException {
        ra.setAngles(alpha, beta, gamma, epsilon);
        Vector3 ep = ra.getEndpoint();
        RoboterArm ra2 = new RoboterArm(1, new Vector3(0, 0, 0));
        ra2.setAngles(alpha + 0.1, beta + 0.1, gamma + 0.1, epsilon + 0.1);
        ra2.setEndpoint(ep);
        System.out.println(ra2.getEndpoint().substract(ep));
        double[] angles = ra2.getAngles();
        assertEquals(alpha, angles[0], 0.01);
        assertEquals(beta, angles[1], 0.01);
        assertEquals(gamma, angles[2], 0.01);
        assertEquals(epsilon, angles[3], 0.01);
    }

    private static double getNextAngle() {
        return (sr.nextDouble() - 0.5) * Math.PI * 1.9;
    }
}
